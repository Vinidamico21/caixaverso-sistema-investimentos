package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.PerfilRiscoResponseDTO;
import br.com.caixaverso.invest.application.dto.ProdutoRecomendadoDTO;
import br.com.caixaverso.invest.application.dto.RecomendacaoResponseDTO;
import br.com.caixaverso.invest.application.port.out.*;
import br.com.caixaverso.invest.domain.model.*;
import br.com.caixaverso.invest.application.port.in.CalcularPerfilRiscoUseCase;
import br.com.caixaverso.invest.application.port.in.RecomendarProdutosUseCase;
import br.com.caixaverso.invest.infra.exception.BusinessException;
import br.com.caixaverso.invest.infra.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static br.com.caixaverso.invest.domain.constants.PerfilConstantes.*;

@ApplicationScoped
public class MotorRecomendacaoService
        implements CalcularPerfilRiscoUseCase, RecomendarProdutosUseCase {

    private static final Logger LOG = Logger.getLogger(MotorRecomendacaoService.class);

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final int LIMITE_PADRAO = 5;

    @Inject
    PerfilRiscoRegraPort perfilRegraPort;
    @Inject
    PreferenciaRegraPort preferenciaRegraPort;
    @Inject
    FrequenciaInvestRegraPort freqInvestRegraPort;
    @Inject
    FrequenciaSimulacaoRegraPort freqSimRegraPort;
    @Inject
    ProdutoRiscoRegraPort riscoRegraPort;

    @Inject SimulacaoInvestimentoPort simulacaoPort;
    @Inject
    InvestimentoPort investimentoPort;
    @Inject
    ProdutoInvestimentoPort produtoPort;

    // =============================================================
    //                     CÁLCULO DO PERFIL
    // =============================================================
    public PerfilRiscoResponseDTO calcularPerfil(Long clienteId) {

        validarClienteId(clienteId);

        LOG.infof("Iniciando calculo de perfil | clienteId=%d", clienteId);

        List<SimulacaoInvestimento> sims = simulacaoPort.listar().stream()
                .filter(s -> s.getCliente().getId().equals(clienteId))
                .toList();

        int scorePreferencia = calcularPreferencias(sims);
        int scoreVolume = calcularVolume(sims);
        int scoreFrequencia = calcularPontuacaoFrequencia(clienteId);

        int scoreFinal = scorePreferencia + scoreVolume + scoreFrequencia;

        LOG.infof(
                "Score final=%d (preferencia=%d, volume=%d, frequencia=%d)",
                scoreFinal, scorePreferencia, scoreVolume, scoreFrequencia
        );

        String perfilNome = classificarPerfilViaBanco(scoreFinal);

        LOG.infof("Perfil classificado=%s", perfilNome);

        return PerfilRiscoResponseDTO.builder()
                .clienteId(clienteId)
                .pontuacao(String.valueOf(scoreFinal))
                .perfil(perfilNome)
                .descricao(gerarDescricao(perfilNome))
                .build();
    }

    // =============================================================
    //              MOTOR DE RECOMENDAÇÃO DE PRODUTOS
    // =============================================================
    public RecomendacaoResponseDTO recomendarParaCliente(Long clienteId) {

        LOG.infof("Iniciando recomendacao | clienteId=%d", clienteId);

        try {
            PerfilRiscoResponseDTO perfil = calcularPerfil(clienteId);

            String perfilUpper = perfil.getPerfil().toUpperCase();
            List<ProdutoInvestimento> produtos = produtoPort.findAll();

            LOG.infof("Produtos carregados=%d | perfil=%s",
                    produtos.size(), perfilUpper);

            List<ProdutoRecomendadoDTO> recomendados =
                    calcularRecomendacoesPorPerfil(perfilUpper, produtos);

            LOG.infof("Total recomendados=%d", recomendados.size());

            return RecomendacaoResponseDTO.builder()
                    .clienteId(clienteId)
                    .perfilRisco(perfilUpper)
                    .scoreRisco(perfil.getPontuacao())
                    .produtos(recomendados)
                    .build();

        } catch (NotFoundException e) {
            // aqui a regra de negócio é diferente: não quebra o endpoint,
            // apenas devolve perfil desconhecido e lista vazia
            LOG.warnf("Perfil nao encontrado para recomendacao | clienteId=%d", clienteId);
            return RecomendacaoResponseDTO.builder()
                    .clienteId(clienteId)
                    .perfilRisco(PERFIL_DESCONHECIDO)
                    .produtos(List.of())
                    .build();
        }
    }

    public List<ProdutoRecomendadoDTO> recomendarPorPerfil(String perfilTexto) {
        String perfilUpper = perfilTexto == null ? PERFIL_CONSERVADOR : perfilTexto.trim().toUpperCase();
        LOG.infof("Recomendacao direta por perfil=%s", perfilUpper);
        return calcularRecomendacoesPorPerfil(perfilUpper, produtoPort.findAll());
    }

    // =============================================================
    //               PIPELINE DE AVALIAÇÃO E FILTRO
    // =============================================================
    private List<ProdutoRecomendadoDTO> calcularRecomendacoesPorPerfil(
            String perfilUpper,
            List<ProdutoInvestimento> produtos) {

        LOG.infof("Iniciando pipeline de recomendação | perfil=%s | totalProdutos=%d",
                perfilUpper, produtos.size());

        return produtos.stream()

                .peek(p -> LOG.debugf("Produto | id=%d | nome=%s | taxa=%s",
                        p.getId(), p.getNome(), p.getTaxaAnual()))

                .filter(this::produtoAtivo)

                .map(p -> {
                    String risco = riscoRegraPort.classificar(p.getTaxaAnual());
                    p.setRisco(risco);
                    LOG.debugf("Risco | %s -> %s", p.getNome(), risco);
                    return p;
                })

                .filter(p -> riscoDentroDoPerfil(p.getRisco(), perfilUpper))

                .map(p -> {
                    BigDecimal score = calcularScoreProduto(p, perfilUpper);
                    LOG.debugf("Score | %s -> %s", p.getNome(), score);
                    return new ProdutoComScore(p, score);
                })

                .sorted(Comparator.comparing(ProdutoComScore::score).reversed())
                .limit(LIMITE_PADRAO)

                .peek(pcs -> LOG.infof("Selecionado | %s | score=%s",
                        pcs.produto().getNome(), pcs.score()))

                .map(this::paraDto)
                .toList();
    }

    private boolean produtoAtivo(ProdutoInvestimento p) {
        return p.getAtivo() == null || p.getAtivo();
    }

    private record ProdutoComScore(ProdutoInvestimento produto, BigDecimal score) {}

    // =============================================================
    //      DEMAIS FUNÇÕES AUXILIARES
    // =============================================================

    private void validarClienteId(Long clienteId) {
        if (clienteId == null) {
            throw new BusinessException("Parâmetro 'clienteId' é obrigatório.");
        }
        if (clienteId <= 0) {
            throw new BusinessException("Parâmetro 'clienteId' deve ser maior que zero.");
        }
    }

    private int calcularPreferencias(List<SimulacaoInvestimento> simulacoes) {
        int prefLiq = 0;
        int prefRent = 0;

        for (SimulacaoInvestimento s : simulacoes) {
            ProdutoInvestimento p = s.getProduto();
            if (p == null) continue;

            boolean altaLiq = isAltaLiquidez(p.getLiquidez(), p.getPrazoMinMeses());
            boolean altaRent = isAltaRentabilidade(p.getTaxaAnual());

            if (altaLiq && !altaRent) prefLiq++;
            else if (altaRent && !altaLiq) prefRent++;
        }

        String preferencia =
                prefRent > prefLiq ? PREF_RENTABILIDADE :
                        prefRent < prefLiq ? PREF_LIQUIDEZ :
                                PREF_EMPATE;

          return preferenciaRegraPort.buscarPontuacao(preferencia);
    }

    private int calcularVolume(List<SimulacaoInvestimento> simulacoes) {
        BigDecimal volume = simulacoes.stream()
                .map(SimulacaoInvestimento::getValorAplicado)
                .reduce(ZERO, BigDecimal::add);

        if (volume.compareTo(new BigDecimal("50000")) > 0) return 30;
        if (volume.compareTo(new BigDecimal("25000")) > 0) return 22;
        if (volume.compareTo(new BigDecimal("10000")) > 0) return 15;
        if (volume.compareTo(new BigDecimal("5000")) > 0)  return 10;
        return 5;
    }

    private int calcularPontuacaoFrequencia(Long clienteId) {

        LocalDate limite = LocalDate.now().minusMonths(6);

        List<Investimento> reais =
                investimentoPort.findByClienteIdAndPeriodo(clienteId, limite);

        List<SimulacaoInvestimento> sims6m =
                simulacaoPort.listar().stream()
                        .filter(s -> s.getCliente().getId().equals(clienteId))
                        .filter(s -> s.getDataSimulacao().toLocalDate().isAfter(limite))
                        .toList();

        int pontosInvest = freqInvestRegraPort.buscarPontuacao(reais.size());
        int pontosSims = freqSimRegraPort.buscarPontuacao(sims6m.size());

        return pontosInvest + pontosSims;
    }

    private String classificarPerfilViaBanco(int score) {
        return perfilRegraPort.buscarRegraPorScore(score)
                .map(PerfilRiscoRegra::getPerfil)
                .orElse(PERFIL_DESCONHECIDO);
    }

    private boolean isAltaLiquidez(String liquidez, Integer prazo) {
        if (liquidez == null) return false;

        String l = liquidez.toUpperCase();

        return l.contains(LIQ_DIARIA) ||
                l.contains(LIQ_D0) ||
                (prazo != null && prazo <= PRAZO_CURTO);
    }

    private boolean isAltaRentabilidade(BigDecimal taxa) {
        return taxa != null && taxa.compareTo(RENTABILIDADE_MEDIA) > 0;
    }

    private String gerarDescricao(String perfil) {
        return switch (perfil) {
            case PERFIL_CONSERVADOR -> DESC_CONSERVADOR;
            case PERFIL_MODERADO -> DESC_MODERADO;
            case PERFIL_AGRESSIVO -> DESC_AGRESSIVO;
            default -> DESC_DESCONHECIDO;
        };
    }

    private boolean riscoDentroDoPerfil(String riscoProduto, String perfilUpper) {
        if (riscoProduto == null) return false;

        return switch (perfilUpper) {
            case PERFIL_CONSERVADOR -> riscoProduto.equalsIgnoreCase(PERFIL_CONSERVADOR_PRODUTO);
            case PERFIL_MODERADO    -> riscoProduto.equalsIgnoreCase(PERFIL_MODERADO_PRODUTO);
            case PERFIL_AGRESSIVO   -> riscoProduto.equalsIgnoreCase(PERFIL_AGRESSIVO_PRODUTO);
            default -> false;
        };
    }

    private BigDecimal calcularScoreProduto(ProdutoInvestimento p, String perfilUpper) {

        BigDecimal rent = calcularRentabilidadeScore(p.getTaxaAnual());
        BigDecimal liq = calcularLiquidezScore(p.getLiquidez(), p.getPrazoMinMeses());
        BigDecimal risco = compatibilidadeRisco(p.getRisco(), perfilUpper);

        BigDecimal finalScore = switch (perfilUpper) {
            case PERFIL_CONSERVADOR ->
                    liq.multiply(PESO_LIQ_CONSERVADOR)
                            .add(rent.multiply(PESO_RENT_CONSERVADOR))
                            .add(risco.multiply(PESO_RISCO_CONSERVADOR));

            case PERFIL_AGRESSIVO ->
                    liq.multiply(PESO_LIQ_AGRESSIVO)
                            .add(rent.multiply(PESO_RENT_AGRESSIVO))
                            .add(risco.multiply(PESO_RISCO_AGRESSIVO));

            default ->
                    liq.multiply(PESO_LIQ_MODERADO)
                            .add(rent.multiply(PESO_RENT_MODERADO))
                            .add(risco.multiply(PESO_RISCO_MODERADO));
        };

        return finalScore.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularRentabilidadeScore(BigDecimal taxa) {
        if (taxa == null) return SCORE_BAIXO;
        if (taxa.compareTo(RENTABILIDADE_BAIXA) < 0) return SCORE_BAIXO;
        if (taxa.compareTo(RENTABILIDADE_MEDIA) <= 0) return SCORE_MEDIO;
        return SCORE_ALTO;
    }

    private BigDecimal calcularLiquidezScore(String liquidez, Integer prazo) {
        if (liquidez == null) return SCORE_BAIXO;

        String liq = liquidez.toUpperCase();
        int p = prazo == null ? 0 : prazo;

        if (liq.contains(LIQ_DIARIA) || liq.contains(LIQ_D0) || p <= PRAZO_CURTO)
            return SCORE_ALTO;

        if (liq.contains(LIQ_MENSAL) || liq.contains(LIQ_D30) || p <= PRAZO_MEDIO)
            return SCORE_MEDIO;

        return SCORE_BAIXO;
    }

    private BigDecimal compatibilidadeRisco(String riscoProduto, String perfilUpper) {
        if (riscoProduto == null) return SCORE_BAIXO;

        String r = riscoProduto.toUpperCase();

        return switch (perfilUpper) {
            case PERFIL_CONSERVADOR -> switch (r) {
                case PERFIL_CONSERVADOR -> SCORE_ALTO;
                case PERFIL_MODERADO    -> SCORE_MEDIO;
                default -> SCORE_BAIXO;
            };
            case PERFIL_MODERADO -> switch (r) {
                case PERFIL_CONSERVADOR -> SCORE_MEDIO;
                case PERFIL_MODERADO    -> SCORE_ALTO;
                default -> SCORE_MEDIO;
            };
            default -> switch (r) {
                case PERFIL_AGRESSIVO -> SCORE_ALTO;
                case PERFIL_MODERADO  -> SCORE_MEDIO;
                default -> SCORE_BAIXO;
            };
        };
    }

    private ProdutoRecomendadoDTO paraDto(ProdutoComScore pcs) {
        ProdutoInvestimento p = pcs.produto();
        return ProdutoRecomendadoDTO.builder()
                .id(p.getId())
                .nome(p.getNome())
                .tipo(p.getTipo())
                .rentabilidade(p.getTaxaAnual())
                .risco(p.getRisco())
                .build();
    }
}
