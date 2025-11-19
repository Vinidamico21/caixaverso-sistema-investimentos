package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.*;
import br.com.caixaverso.invest.application.port.in.AgruparSimulacoesPorProdutoDiaUseCase;
import br.com.caixaverso.invest.application.port.in.ListarSimulacoesUseCase;
import br.com.caixaverso.invest.application.port.in.SimularInvestimentoUseCase;
import br.com.caixaverso.invest.application.port.out.ClientePort;
import br.com.caixaverso.invest.application.port.out.ProdutoInvestimentoPort;
import br.com.caixaverso.invest.application.port.out.SimulacaoInvestimentoPort;
import br.com.caixaverso.invest.application.dto.request.SimularInvestimentoRequest;
import br.com.caixaverso.invest.application.dto.response.PageResponse;
import br.com.caixaverso.invest.application.dto.response.SimularInvestimentoResponse;
import br.com.caixaverso.invest.infra.exception.NotFoundException;

import br.com.caixaverso.invest.infra.persistence.entity.Cliente;
import br.com.caixaverso.invest.infra.persistence.entity.ProdutoInvestimento;
import br.com.caixaverso.invest.infra.persistence.entity.SimulacaoInvestimento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import static br.com.caixaverso.invest.domain.constants.PerfilConstantes.*;
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

        LOG.infof(LOG_SIM_INICIO,
                request.getClienteId(), request.getTipoProduto(),
                request.getValor(), request.getPrazoMeses());

        Cliente cliente = clientePort.findById(request.getClienteId())
                .orElseThrow(() -> {
                    LOG.warnf(LOG_SIM_CLIENTE_NAO_ENCONTRADO, request.getClienteId());
                    return new NotFoundException(ERRO_CLIENTE_NAO_ENCONTRADO);
                });

        LOG.debugf(LOG_SIM_CLIENTE_ENCONTRADO, cliente.getId());

        List<ProdutoInvestimento> produtos = produtoPort.findByTipo(request.getTipoProduto());

        if (produtos.isEmpty()) {
            LOG.warnf(LOG_SIM_PRODUTOS_NAO_ENCONTRADOS, request.getTipoProduto());
            throw new NotFoundException(ERRO_PRODUTO_TIPO_NAO_ENCONTRADO);
        }

        LOG.infof(LOG_SIM_PRODUTOS_ENCONTRADOS, produtos.size(), request.getTipoProduto());

        ProdutoInvestimento melhorProduto = produtos.stream()
                .max(Comparator.comparing(ProdutoInvestimento::getTaxaAnual))
                .orElseThrow();

        LOG.infof(LOG_SIM_MELHOR_PRODUTO,
                melhorProduto.getId(), melhorProduto.getNome(), melhorProduto.getTaxaAnual());

        BigDecimal valorFinal = calcularJurosCompostos(
                request.getValor(),
                melhorProduto.getTaxaAnual(),
                request.getPrazoMeses()
        );

        LOG.debugf(LOG_SIM_RESULTADO,
                request.getValor(), melhorProduto.getTaxaAnual(),
                request.getPrazoMeses(), valorFinal);

        SimulacaoInvestimento sim = SimulacaoInvestimento.builder()
                .cliente(cliente)
                .produto(melhorProduto)
                .valorAplicado(request.getValor())
                .valorFinal(valorFinal)
                .prazoMeses(request.getPrazoMeses())
                .dataSimulacao(LocalDateTime.now())
                .perfilRiscoCalculado(melhorProduto.getRisco())
                .build();

        LOG.infof(LOG_SIM_PERSISTENCIA,
                cliente.getId(), melhorProduto.getId(),
                sim.getValorAplicado(), sim.getValorFinal());

        simulacaoPort.salvar(sim);

        LOG.debug(LOG_SIM_SALVA);

        SimularInvestimentoResponse response =
                montarRespostaSimulacao(melhorProduto, valorFinal, request.getPrazoMeses());

        LOG.infof(LOG_SIM_FINAL,
                request.getClienteId(), melhorProduto.getNome(),
                valorFinal, request.getPrazoMeses());

        return response;
    }


    private SimularInvestimentoResponse montarRespostaSimulacao(
            ProdutoInvestimento produto, BigDecimal valorFinal, int prazoMeses) {

        LOG.debugf(LOG_SIM_MONTANDO_RESPOSTA,
                Optional.ofNullable(produto.getId()), valorFinal, prazoMeses);

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

        LOG.debug(LOG_SIM_RESPOSTA_OK);

        return response;
    }

    @Override
    public PageResponse<SimulacaoResumoDTO> listarSimulacoes(Long clienteId, int page, int size) {

        LOG.infof(LOG_LISTAR_SIM_INICIO, clienteId, page, size);

        List<SimulacaoInvestimento> sims =
                clienteId == null ? simulacaoPort.listar() : simulacaoPort.listarPorClienteId(clienteId);

        LOG.infof(LOG_LISTAR_SIM_TOTAL, sims.size());

        List<SimulacaoResumoDTO> todosDtos = sims.stream()
                .peek(sim -> LOG.debugf(LOG_LISTAR_SIM_ITEM,
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

        if (size <= 0) size = 20;

        int fromIndex = Math.min(page * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<SimulacaoResumoDTO> pageContent =
                fromIndex > toIndex ? List.of() : todosDtos.subList(fromIndex, toIndex);

        int totalPages = (int) Math.ceil((double) totalElements / size);

        LOG.infof(LOG_LISTAR_SIM_PAGINA, page, size, totalElements, totalPages);

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

        LOG.info(LOG_AGRUP_INICIO);

        List<SimulacaoInvestimento> simulacoes = simulacaoPort.listar();

        LOG.infof(LOG_AGRUP_TOTAL, simulacoes.size());

        Map<String, Map<LocalDate, List<SimulacaoInvestimento>>> agrupado =
                simulacoes.stream()
                        .collect(Collectors.groupingBy(
                                sim -> sim.getProduto().getNome(),
                                Collectors.groupingBy(sim -> {
                                    LocalDate dia = sim.getDataSimulacaoDia();
                                    return dia != null ? dia : sim.getDataSimulacao().toLocalDate();
                                })
                        ));

        List<SimulacaoPorProdutoDiaDTO> resposta = new ArrayList<>();

        for (var entryProduto : agrupado.entrySet()) {

            LOG.debugf(LOG_AGRUP_PROCESSANDO_PRODUTO, entryProduto.getKey());

            for (var entryDia : entryProduto.getValue().entrySet()) {

                LocalDate data = entryDia.getKey();
                List<SimulacaoInvestimento> simsDoDia = entryDia.getValue();

                BigDecimal media = simsDoDia.stream()
                        .map(SimulacaoInvestimento::getValorFinal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(simsDoDia.size()), 2, RoundingMode.HALF_UP);

                LOG.debugf(LOG_AGRUP_REGISTRO,
                        entryProduto.getKey(), data, simsDoDia.size(), media);

                resposta.add(SimulacaoPorProdutoDiaDTO.builder()
                        .produto(entryProduto.getKey())
                        .data(data)
                        .quantidadeSimulacoes((long) simsDoDia.size())
                        .mediaValorFinal(media)
                        .build());
            }
        }

        resposta.sort(
                Comparator.comparing(SimulacaoPorProdutoDiaDTO::getData).reversed()
                        .thenComparing(SimulacaoPorProdutoDiaDTO::getProduto)
        );

        LOG.infof(LOG_AGRUP_FINAL, resposta.size());

        // paginação em memória
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

        LOG.debugf(LOG_JUROS_INICIO, valor, taxaAnual, meses);

        BigDecimal taxaMensal =
                taxaAnual.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal resultado = valor
                .multiply(BigDecimal.ONE.add(taxaMensal).pow(meses))
                .setScale(2, RoundingMode.HALF_UP);

        LOG.debugf(LOG_JUROS_RESULTADO, resultado);

        return resultado;
    }
}
