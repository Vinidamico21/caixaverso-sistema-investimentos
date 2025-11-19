package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.PerfilRiscoResponseDTO;
import br.com.caixaverso.invest.application.dto.ProdutoRecomendadoDTO;
import br.com.caixaverso.invest.application.dto.RecomendacaoResponseDTO;
import br.com.caixaverso.invest.application.port.in.CalcularPerfilRiscoUseCase;
import br.com.caixaverso.invest.application.port.in.RecomendarProdutosUseCase;
import br.com.caixaverso.invest.application.port.out.*;
import br.com.caixaverso.invest.domain.enums.PerfilRisco;
import br.com.caixaverso.invest.domain.enums.PreferenciaPerfil;
import br.com.caixaverso.invest.domain.enums.RiscoProduto;
import br.com.caixaverso.invest.infra.exception.BusinessException;
import br.com.caixaverso.invest.infra.exception.NotFoundException;
import br.com.caixaverso.invest.infra.persistence.entity.Investimento;
import br.com.caixaverso.invest.infra.persistence.entity.PerfilRiscoRegra;
import br.com.caixaverso.invest.infra.persistence.entity.ProdutoInvestimento;
import br.com.caixaverso.invest.infra.persistence.entity.SimulacaoInvestimento;
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

    @Inject PerfilRiscoRegraPort perfilRegraPort;
    @Inject PreferenciaRegraPort preferenciaRegraPort;
    @Inject FrequenciaInvestRegraPort freqInvestRegraPort;
    @Inject FrequenciaSimulacaoRegraPort freqSimRegraPort;
    @Inject ProdutoRiscoRegraPort riscoRegraPort;

    @Inject SimulacaoInvestimentoPort simulacaoPort;
    @Inject InvestimentoPort investimentoPort;
    @Inject ProdutoInvestimentoPort produtoPort;

    // CÁLCULO DE PERFIL
    @Override
    public PerfilRiscoResponseDTO calcularPerfil(Long clienteId) {

        validarClienteId(clienteId);
        LOG.infof(LOG_CALC_INICIO, clienteId);

        List<SimulacaoInvestimento> sims = simulacaoPort.listar().stream()
                .filter(s -> s.getCliente().getId().equals(clienteId))
                .toList();

        int scorePreferencia = calcularPreferencias(sims);
        int scoreVolume = calcularVolume(sims);
        int scoreFrequencia = calcularPontuacaoFrequencia(clienteId);

        int scoreFinal = scorePreferencia + scoreVolume + scoreFrequencia;

        LOG.infof(LOG_CALC_SCORE_FINAL, scoreFinal, scorePreferencia, scoreVolume, scoreFrequencia);

        String perfilBanco = classificarPerfilViaBanco(scoreFinal);
        PerfilRisco perfil = PerfilRisco.from(perfilBanco);

        LOG.infof(LOG_CALC_CLASSIFICADO, perfil.name());

        return PerfilRiscoResponseDTO.builder()
                .clienteId(clienteId)
                .pontuacao(String.valueOf(scoreFinal))
                .perfil(perfil.name())
                .descricao(gerarDescricao(perfil))
                .build();
    }

    // RECOMENDAÇÃO
    @Override
    public RecomendacaoResponseDTO recomendarParaCliente(Long clienteId) {

        LOG.infof(LOG_RECOM_INICIO, clienteId);

        try {
            PerfilRiscoResponseDTO perfilDTO = calcularPerfil(clienteId);
            PerfilRisco perfilRisco = PerfilRisco.from(perfilDTO.getPerfil());

            List<ProdutoInvestimento> produtos = produtoPort.findAll();

            LOG.infof(LOG_RECOM_PRODUTOS_CARREGADOS, produtos.size(), perfilRisco.name());

            List<ProdutoRecomendadoDTO> recomendados =
                    calcularRecomendacoesPorPerfil(perfilRisco, produtos);

            LOG.infof(LOG_RECOM_TOTAL, recomendados.size());

            return RecomendacaoResponseDTO.builder()
                    .clienteId(clienteId)
                    .perfilRisco(perfilRisco.name())
                    .scoreRisco(perfilDTO.getPontuacao())
                    .produtos(recomendados)
                    .build();

        } catch (NotFoundException e) {
            LOG.warnf(LOG_RECOM_PERFIL_NAO_ENCONTRADO, clienteId);
            return RecomendacaoResponseDTO.builder()
                    .clienteId(clienteId)
                    .perfilRisco(PerfilRisco.DESCONHECIDO.name())
                    .produtos(List.of())
                    .build();
        }
    }

    @Override
    public List<ProdutoRecomendadoDTO> recomendarPorPerfil(String perfilTexto) {
        PerfilRisco perfil = PerfilRisco.from(perfilTexto);

        LOG.infof(LOG_RECOM_DIRETA, perfil.name());

        return calcularRecomendacoesPorPerfil(perfil, produtoPort.findAll());
    }

    //PIPELINE PRINCIPAL
    private List<ProdutoRecomendadoDTO> calcularRecomendacoesPorPerfil(
            PerfilRisco perfil,
            List<ProdutoInvestimento> produtos) {

        LOG.infof(LOG_PIPELINE_INICIO, perfil.name(), produtos.size());

        return produtos.stream()

                .peek(p -> LOG.debugf(LOG_PIPELINE_PRODUTO,
                        p.getId(), p.getNome(), p.getTaxaAnual()))

                .filter(this::produtoAtivo)

                .map(p -> {
                    String riscoStr = riscoRegraPort.classificar(p.getTaxaAnual());
                    RiscoProduto riscoEnum = RiscoProduto.from(riscoStr);
                    p.setRisco(riscoEnum.name());
                    LOG.debugf(LOG_PIPELINE_RISCO, p.getNome(), riscoEnum);
                    return p;
                })

                .filter(p -> riscoDentroDoPerfil(RiscoProduto.from(p.getRisco()), perfil))

                .map(p -> new ProdutoComScore(p, calcularScoreProduto(p, perfil)))

                .sorted(Comparator.comparing(ProdutoComScore::score).reversed())
                .limit(LIMITE_PADRAO_RECOMENDACAO)

                .peek(pcs -> LOG.infof(LOG_PIPELINE_SELECIONADO,
                        pcs.produto().getNome(), pcs.score()))

                .map(this::paraDto)
                .toList();
    }

    private boolean produtoAtivo(ProdutoInvestimento p) {
        return p.getAtivo() == null || p.getAtivo();
    }

    private record ProdutoComScore(ProdutoInvestimento produto, BigDecimal score) {}

    // AUXILIARES DO PERFIL
    private void validarClienteId(Long clienteId) {
        if (clienteId == null) throw new BusinessException(ERRO_CLIENTE_ID_OBRIGATORIO);
        if (clienteId <= 0) throw new BusinessException(ERRO_CLIENTE_ID_INVALIDO);
    }

    private int calcularPreferencias(List<SimulacaoInvestimento> simulacoes) {
        int liq = 0;
        int rent = 0;

        for (SimulacaoInvestimento s : simulacoes) {
            ProdutoInvestimento p = s.getProduto();
            if (p == null) continue;

            boolean altaLiq = isAltaLiquidez(p.getLiquidez(), p.getPrazoMinMeses());
            boolean altaRent = isAltaRentabilidade(p.getTaxaAnual());

            if (altaLiq && !altaRent) liq++;
            else if (altaRent && !altaLiq) rent++;
        }

        PreferenciaPerfil pref =
                rent > liq ? PreferenciaPerfil.RENTABILIDADE :
                        rent < liq ? PreferenciaPerfil.LIQUIDEZ :
                                PreferenciaPerfil.EMPATE;

        return preferenciaRegraPort.buscarPontuacao(pref.name());
    }

    private int calcularVolume(List<SimulacaoInvestimento> sims) {
        BigDecimal volume = sims.stream()
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

        return freqInvestRegraPort.buscarPontuacao(reais.size())
                + freqSimRegraPort.buscarPontuacao(sims6m.size());
    }

    private String classificarPerfilViaBanco(int score) {
        return perfilRegraPort.buscarRegraPorScore(score)
                .map(PerfilRiscoRegra::getPerfil)
                .orElse(PerfilRisco.DESCONHECIDO.name());
    }

    private boolean isAltaLiquidez(String liquidezStr, Integer prazo) {
        if (liquidezStr == null) return false;

        String l = liquidezStr.toUpperCase();
        return l.contains(LIQ_D0) ||
                l.contains(LIQ_DIARIA) ||
                (prazo != null && prazo <= PRAZO_CURTO);
    }

    private boolean isAltaRentabilidade(BigDecimal taxa) {
        return taxa != null && taxa.compareTo(RENTABILIDADE_MEDIA) > 0;
    }

    private String gerarDescricao(PerfilRisco perfil) {
        return switch (perfil) {
            case CONSERVADOR -> DESC_CONSERVADOR;
            case MODERADO -> DESC_MODERADO;
            case AGRESSIVO -> DESC_AGRESSIVO;
            default -> DESC_DESCONHECIDO;
        };
    }

    private boolean riscoDentroDoPerfil(RiscoProduto risco, PerfilRisco perfil) {
        return switch (perfil) {
            case CONSERVADOR -> risco == RiscoProduto.BAIXO;
            case MODERADO -> risco == RiscoProduto.MEDIO;
            case AGRESSIVO -> risco == RiscoProduto.ALTO;
            default -> false;
        };
    }

    private BigDecimal calcularScoreProduto(ProdutoInvestimento p, PerfilRisco perfil) {

        BigDecimal rent = calcularRentabilidadeScore(p.getTaxaAnual());
        BigDecimal liq = calcularLiquidezScore(p.getLiquidez(), p.getPrazoMinMeses());
        BigDecimal risco = compatibilidadeRisco(RiscoProduto.from(p.getRisco()), perfil);

        return (
                switch (perfil) {
                    case CONSERVADOR ->
                            liq.multiply(PESO_LIQ_CONSERVADOR)
                                    .add(rent.multiply(PESO_RENT_CONSERVADOR))
                                    .add(risco.multiply(PESO_RISCO_CONSERVADOR));

                    case AGRESSIVO ->
                            liq.multiply(PESO_LIQ_AGRESSIVO)
                                    .add(rent.multiply(PESO_RENT_AGRESSIVO))
                                    .add(risco.multiply(PESO_RISCO_AGRESSIVO));

                    default ->
                            liq.multiply(PESO_LIQ_MODERADO)
                                    .add(rent.multiply(PESO_RENT_MODERADO))
                                    .add(risco.multiply(PESO_RISCO_MODERADO));
                }
        ).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularRentabilidadeScore(BigDecimal taxa) {
        if (taxa == null) return SCORE_BAIXO;
        if (taxa.compareTo(RENTABILIDADE_BAIXA) < 0) return SCORE_BAIXO;
        if (taxa.compareTo(RENTABILIDADE_MEDIA) <= 0) return SCORE_MEDIO;
        return SCORE_ALTO;
    }

    private BigDecimal calcularLiquidezScore(String liquidezStr, Integer prazo) {
        if (liquidezStr == null) return SCORE_BAIXO;

        liquidezStr = liquidezStr.toUpperCase();
        int p = prazo == null ? 0 : prazo;

        if (liquidezStr.contains(LIQ_DIARIA) || liquidezStr.contains(LIQ_D0) || p <= PRAZO_CURTO)
            return SCORE_ALTO;

        if (liquidezStr.contains(LIQ_MENSAL) || liquidezStr.contains(LIQ_D30) || p <= PRAZO_MEDIO)
            return SCORE_MEDIO;

        return SCORE_BAIXO;
    }

    private BigDecimal compatibilidadeRisco(RiscoProduto risco, PerfilRisco perfil) {
        return switch (perfil) {
            case CONSERVADOR -> switch (risco) {
                case BAIXO -> SCORE_ALTO;
                case MEDIO -> SCORE_MEDIO;
                default -> SCORE_BAIXO;
            };
            case MODERADO -> switch (risco) {
                case BAIXO -> SCORE_MEDIO;
                case MEDIO -> SCORE_ALTO;
                default -> SCORE_MEDIO;
            };
            default -> switch (risco) {
                case ALTO -> SCORE_ALTO;
                case MEDIO -> SCORE_MEDIO;
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
