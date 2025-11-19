package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.*;
import br.com.caixaverso.invest.domain.model.*;
import br.com.caixaverso.invest.application.port.in.AgruparSimulacoesPorProdutoDiaUseCase;
import br.com.caixaverso.invest.application.port.in.ListarSimulacoesUseCase;
import br.com.caixaverso.invest.application.port.in.SimularInvestimentoUseCase;
import br.com.caixaverso.invest.application.port.out.ClientePort;
import br.com.caixaverso.invest.application.port.out.ProdutoInvestimentoPort;
import br.com.caixaverso.invest.application.port.out.SimulacaoInvestimentoPort;
import br.com.caixaverso.invest.infra.exception.NotFoundException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import static br.com.caixaverso.invest.infra.util.RiscoMapper.mapearRiscoHumano;

@ApplicationScoped
public class SimulacaoService implements
        SimularInvestimentoUseCase,
        ListarSimulacoesUseCase,
        AgruparSimulacoesPorProdutoDiaUseCase {

    private static final Logger LOG = Logger.getLogger(SimulacaoService.class);

    @Inject
    ClientePort clientePort;

    @Inject
    ProdutoInvestimentoPort produtoPort;

    @Inject
    SimulacaoInvestimentoPort simulacaoPort;

    // EXECUTA SIMULAÇÃO
    @Transactional
    public SimularInvestimentoResponse executarSimulacao(SimularInvestimentoRequest request) {

        LOG.infof("Iniciando simulacao | clienteId=%d | tipoProduto=%s | valor=%s | prazoMeses=%d",
                request.getClienteId(), request.getTipoProduto(), request.getValor(), request.getPrazoMeses());

        Cliente cliente = clientePort.findById(request.getClienteId())
                .orElseThrow(() -> {
                    LOG.warnf("Cliente nao encontrado para simulacao | clienteId=%d", request.getClienteId());
                    return new NotFoundException("Cliente não encontrado.");
                });

        LOG.debugf("Cliente encontrado para simulacao | clienteId=%d", cliente.getId());

        List<ProdutoInvestimento> produtos = produtoPort.findByTipo(request.getTipoProduto());

        if (produtos.isEmpty()) {
            LOG.warnf("Nenhum produto encontrado para o tipo solicitado | tipoProduto=%s", request.getTipoProduto());
            throw new NotFoundException("Nenhum produto encontrado para o tipo solicitado");
        }

        LOG.infof("Produtos encontrados para simulacao=%d | tipoProduto=%s",
                produtos.size(), request.getTipoProduto());

        ProdutoInvestimento melhorProduto = produtos.stream()
                .max(Comparator.comparing(ProdutoInvestimento::getTaxaAnual))
                .orElseThrow();

        LOG.infof("Melhor produto selecionado | produtoId=%d | nome=%s | taxaAnual=%s",
                melhorProduto.getId(), melhorProduto.getNome(), melhorProduto.getTaxaAnual());

        BigDecimal valorFinal = calcularJurosCompostos(
                request.getValor(),
                melhorProduto.getTaxaAnual(),
                request.getPrazoMeses()
        );

        LOG.debugf("Resultado da simulacao | valorInicial=%s | taxaAnual=%s | prazoMeses=%d | valorFinal=%s",
                request.getValor(), melhorProduto.getTaxaAnual(), request.getPrazoMeses(), valorFinal);

        SimulacaoInvestimento sim = SimulacaoInvestimento.builder()
                .cliente(cliente)
                .produto(melhorProduto)
                .valorAplicado(request.getValor())
                .valorFinal(valorFinal)
                .prazoMeses(request.getPrazoMeses())
                .dataSimulacao(LocalDateTime.now())
                .perfilRiscoCalculado(melhorProduto.getRisco())
                .build();

        LOG.infof("Persistindo simulacao de investimento | clienteId=%d | produtoId=%d | valorAplicado=%s | valorFinal=%s",
                cliente.getId(), melhorProduto.getId(), sim.getValorAplicado(), sim.getValorFinal());

        simulacaoPort.salvar(sim);

        LOG.debug("Simulacao de investimento salva com sucesso.");

        SimularInvestimentoResponse response = montarRespostaSimulacao(melhorProduto, valorFinal, request.getPrazoMeses());

        LOG.infof("Simulacao concluida | clienteId=%d | produto=%s | valorFinal=%s | prazoMeses=%d",
                request.getClienteId(), melhorProduto.getNome(), valorFinal, request.getPrazoMeses());

        return response;
    }

    private SimularInvestimentoResponse montarRespostaSimulacao(
            ProdutoInvestimento produto, BigDecimal valorFinal, int prazoMeses) {

        LOG.debugf(
                "Montando resposta de simulacao | produtoId=%d | valorFinal=%s | prazoMeses=%d",
                produto.getId().longValue(),
                prazoMeses
        );

        ProdutoValidadoDTO produtoDto = ProdutoValidadoDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .tipo(produto.getTipo())
                .rentabilidade(produto.getTaxaAnual())
                .risco(mapearRiscoHumano(produto.getRisco()))
                .build();

        ResultadoSimulacaoDTO resultadoDTO = ResultadoSimulacaoDTO.builder()
                .valorFinal(valorFinal)
                .rentabilidadeEfetiva(produto.getTaxaAnual())
                .prazoMeses(prazoMeses)
                .build();

        SimularInvestimentoResponse response = SimularInvestimentoResponse.builder()
                .produtoValidado(produtoDto)
                .resultadoSimulacao(resultadoDTO)
                .dataSimulacao(OffsetDateTime.now(ZoneOffset.UTC))
                .build();

        LOG.debug("Resposta de simulacao montada com sucesso.");

        return response;
    }

    @Override
    public PageResponse<SimulacaoResumoDTO> listarSimulacoes(Long clienteId, int page, int size) {

        LOG.infof("Listando simulacoes | filtroClienteId=%s | page=%d | size=%d",
                clienteId, page, size);

        // carrega simulações
        List<SimulacaoInvestimento> sims =
                clienteId == null ? simulacaoPort.listar() : simulacaoPort.listarPorClienteId(clienteId);

        LOG.infof("Total de simulacoes encontradas=%d", sims.size());

        // mapeia para DTOs
        List<SimulacaoResumoDTO> todosDtos = sims.stream()
                .peek(sim -> LOG.debugf(
                        "Simulacao encontrada | id=%d | clienteId=%d | produto=%s | valorAplicado=%s | valorFinal=%s",
                        sim.getId(),
                        sim.getCliente().getId(),
                        sim.getProduto().getNome(),
                        sim.getValorAplicado(),
                        sim.getValorFinal()
                ))
                .map(sim -> SimulacaoResumoDTO.builder()
                        .id(sim.getId())
                        .clienteId(sim.getCliente().getId())
                        .produto(sim.getProduto().getNome())
                        .valorInvestido(sim.getValorAplicado())
                        .valorFinal(sim.getValorFinal())
                        .prazoMeses(sim.getPrazoMeses())
                        .dataSimulacao(sim.getDataSimulacao().atOffset(ZoneOffset.UTC))
                        .build())
                .toList();

        int totalElements = todosDtos.size();

        if (size <= 0) {
            size = 20;
        }

        int fromIndex = Math.min(page * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<SimulacaoResumoDTO> pageContent =
                fromIndex > toIndex ? List.of() : todosDtos.subList(fromIndex, toIndex);

        int totalPages = (int) Math.ceil((double) totalElements / size);

        LOG.infof("Pagina gerada | page=%d | size=%d | totalElements=%d | totalPages=%d",
                page, size, totalElements, totalPages);

        return PageResponse.<SimulacaoResumoDTO>builder()
                .content(pageContent)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }

    // AGRUPAMENTO por PRODUTO/DIA
    @Override
    public PageResponse<SimulacaoPorProdutoDiaDTO> agrupamentoPorProdutoDia(int page, int size) {

        LOG.info("Iniciando agrupamento de simulacoes por produto e dia.");

        List<SimulacaoInvestimento> simulacoes = simulacaoPort.listar();

        LOG.infof("Total de simulacoes carregadas para agrupamento=%d", simulacoes.size());

        Map<String, Map<LocalDate, List<SimulacaoInvestimento>>> agrupado =
                simulacoes.stream()
                        .collect(Collectors.groupingBy(
                                sim -> sim.getProduto().getNome(), // ainda precisa da entidade ProdutoInvestimento OK
                                Collectors.groupingBy(sim -> {
                                    LocalDate dia = sim.getDataSimulacaoDia();
                                    if (dia != null) {
                                        return dia;
                                    }
                                    return sim.getDataSimulacao().toLocalDate();
                                })
                        ));

        List<SimulacaoPorProdutoDiaDTO> resposta = new ArrayList<>();

        for (var entryProduto : agrupado.entrySet()) {
            String produto = entryProduto.getKey();

            LOG.debugf("Processando agrupamento para produto=%s", produto);

            for (var entryDia : entryProduto.getValue().entrySet()) {
                LocalDate data = entryDia.getKey();
                List<SimulacaoInvestimento> simsDoDia = entryDia.getValue();

                BigDecimal media = simsDoDia.stream()
                        .map(SimulacaoInvestimento::getValorFinal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(simsDoDia.size()), 2, RoundingMode.HALF_UP);

                LOG.debugf("Agrupamento gerado | produto=%s | data=%s | qtdSimulacoes=%d | mediaValorFinal=%s",
                        produto, data, simsDoDia.size(), media);

                resposta.add(SimulacaoPorProdutoDiaDTO.builder()
                        .produto(produto)
                        .data(data)
                        .quantidadeSimulacoes((long) simsDoDia.size())
                        .mediaValorFinal(media)
                        .build());
            }
        }

        resposta.sort(Comparator
                .comparing(SimulacaoPorProdutoDiaDTO::getData).reversed()
                .thenComparing(SimulacaoPorProdutoDiaDTO::getProduto));

        LOG.infof("Total de registros de agrupamento por produto/dia gerados=%d", resposta.size());

        // ---- paginação em memória ----
        int totalElements = resposta.size();
        int fromIndex = Math.min(page * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<SimulacaoPorProdutoDiaDTO> pageContent =
                fromIndex > toIndex ? List.of() : resposta.subList(fromIndex, toIndex);

        int totalPages = (int) Math.ceil((double) totalElements / size);

        return PageResponse.<SimulacaoPorProdutoDiaDTO>builder()
                .content(pageContent)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }

    // CÁLCULO
    private BigDecimal calcularJurosCompostos(BigDecimal valor, BigDecimal taxaAnual, int meses) {

        LOG.debugf("Calculando juros compostos | valor=%s | taxaAnual=%s | meses=%d",
                valor, taxaAnual, meses);

        BigDecimal taxaMensal = taxaAnual.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal resultado = valor
                .multiply(BigDecimal.ONE.add(taxaMensal).pow(meses))
                .setScale(2, RoundingMode.HALF_UP);

        LOG.debugf("Juros compostos calculados | resultado=%s", resultado);

        return resultado;
    }
}
