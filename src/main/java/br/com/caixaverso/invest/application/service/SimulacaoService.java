package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.*;
import br.com.caixaverso.invest.domain.model.*;
import br.com.caixaverso.invest.domain.port.*;
import br.com.caixaverso.invest.infra.exception.BusinessException;
import br.com.caixaverso.invest.infra.exception.NotFoundException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static br.com.caixaverso.invest.infra.util.RiscoMapper.mapearRiscoHumano;

@ApplicationScoped
public class SimulacaoService {

    @Inject
    ClientePort clientePort;

    @Inject
    ProdutoInvestimentoPort produtoPort;

    @Inject
    SimulacaoInvestimentoPort simulacaoPort;

    // EXECUTA SIMULAÇÃO
    @Transactional
    public SimularInvestimentoResponse executarSimulacao(SimularInvestimentoRequest request) {

        Cliente cliente = clientePort.findById(request.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        List<ProdutoInvestimento> produtos = produtoPort.findByTipo(request.getTipoProduto());

        if (produtos.isEmpty()) {
            throw new NotFoundException("Nenhum produto encontrado para o tipo solicitado");
        }

        ProdutoInvestimento melhorProduto = produtos.stream()
                .max(Comparator.comparing(ProdutoInvestimento::getTaxaAnual))
                .orElseThrow();

        BigDecimal valorFinal = calcularJurosCompostos(
                request.getValor(),
                melhorProduto.getTaxaAnual(),
                request.getPrazoMeses()
        );

        SimulacaoInvestimento sim = SimulacaoInvestimento.builder()
                .cliente(cliente)
                .produto(melhorProduto)
                .valorAplicado(request.getValor())
                .valorFinal(valorFinal)
                .prazoMeses(request.getPrazoMeses())
                .dataSimulacao(LocalDateTime.now())
                .perfilRiscoCalculado(melhorProduto.getRisco())
                .build();

        simulacaoPort.salvar(sim);

        return montarRespostaSimulacao(melhorProduto, valorFinal, request.getPrazoMeses());
    }

    private SimularInvestimentoResponse montarRespostaSimulacao(
            ProdutoInvestimento produto, BigDecimal valorFinal, int prazoMeses) {

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

        return SimularInvestimentoResponse.builder()
                .produtoValidado(produtoDto)
                .resultadoSimulacao(resultadoDTO)
                .dataSimulacao(OffsetDateTime.now(ZoneOffset.UTC))
                .build();
    }

    // LISTAR SIMULAÇÕES
    public List<SimulacaoResumoDTO> listarSimulacoes(Long clienteId) {
        List<SimulacaoInvestimento> sims =
                clienteId == null ? simulacaoPort.listar() : simulacaoPort.listarPorClienteId(clienteId);

        return sims.stream().map(sim -> SimulacaoResumoDTO.builder()
                .id(sim.getId())
                .clienteId(sim.getCliente().getId())
                .produto(sim.getProduto().getNome())
                .valorInvestido(sim.getValorAplicado())
                .valorFinal(sim.getValorFinal())
                .prazoMeses(sim.getPrazoMeses())
                .dataSimulacao(sim.getDataSimulacao().atOffset(ZoneOffset.UTC))
                .build()).toList();
    }

    // AGRUPAMENTO por PRODUTO/DIA
    public List<SimulacaoPorProdutoDiaDTO> agrupamentoPorProdutoDia() {

        List<SimulacaoInvestimento> simulacoes = simulacaoPort.listar();

        Map<String, Map<LocalDate, List<SimulacaoInvestimento>>> agrupado =
                simulacoes.stream().collect(Collectors.groupingBy(
                        sim -> sim.getProduto().getNome(),
                        Collectors.groupingBy(sim -> sim.getDataSimulacao().toLocalDate())
                ));

        List<SimulacaoPorProdutoDiaDTO> resposta = new ArrayList<>();

        for (var entryProduto : agrupado.entrySet()) {
            String produto = entryProduto.getKey();

            for (var entryDia : entryProduto.getValue().entrySet()) {
                LocalDate data = entryDia.getKey();
                List<SimulacaoInvestimento> simsDoDia = entryDia.getValue();

                BigDecimal media = simsDoDia.stream()
                        .map(SimulacaoInvestimento::getValorFinal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(simsDoDia.size()), 2, RoundingMode.HALF_UP);

                resposta.add(SimulacaoPorProdutoDiaDTO.builder()
                        .produto(produto)
                        .data(data)
                        .quantidadeSimulacoes((long) simsDoDia.size())
                        .mediaValorFinal(media)
                        .build());
            }
        }

        return resposta;
    }

    // CÁLCULO
    private BigDecimal calcularJurosCompostos(BigDecimal valor, BigDecimal taxaAnual, int meses) {

        BigDecimal taxaMensal = taxaAnual.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        return valor
                .multiply(BigDecimal.ONE.add(taxaMensal).pow(meses))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
