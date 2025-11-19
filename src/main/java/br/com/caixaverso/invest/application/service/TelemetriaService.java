package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.TelemetriaPeriodoDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaResponseDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaServicoDTO;
import br.com.caixaverso.invest.domain.model.TelemetriaRegistro;
import br.com.caixaverso.invest.application.port.in.GerarRelatorioTelemetriaUseCase;
import br.com.caixaverso.invest.application.port.in.RegistrarTelemetriaUseCase;
import br.com.caixaverso.invest.application.port.out.TelemetriaPort;
import br.com.caixaverso.invest.infra.exception.BusinessException;
import br.com.caixaverso.invest.infra.repository.TelemetriaRegistroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TelemetriaService implements
        GerarRelatorioTelemetriaUseCase,
        RegistrarTelemetriaUseCase {

    private static final Logger LOG = Logger.getLogger(TelemetriaService.class);

    @Inject
    TelemetriaPort telemetriaPort;

    @Inject
    TelemetriaRegistroRepository telemetriaRepository;

    // =============================================================
    //                    REGISTRO DE TELEMETRIA
    // =============================================================
    @Transactional
    public void registrar(
            Long clienteId,
            String endpoint,
            String metodoHttp,
            boolean sucesso,
            int statusHttp,
            int duracaoMs
    ) {
        validarRegistro(endpoint, metodoHttp, statusHttp, duracaoMs);

        LOG.debugf("Registrando telemetria | endpoint=%s | metodo=%s | status=%d | duracaoMs=%d",
                endpoint, metodoHttp, statusHttp, duracaoMs);

        TelemetriaRegistro reg = TelemetriaRegistro.builder()
                .endpoint(endpoint)
                .metodoHttp(metodoHttp)
                .sucesso(sucesso)
                .statusHttp(statusHttp)
                .duracaoMs(duracaoMs)
                .build();

        telemetriaPort.salvar(reg);
    }

    private void validarRegistro(String endpoint, String metodoHttp, int statusHttp, int duracaoMs) {
        if (endpoint == null || endpoint.isBlank()) {
            throw new BusinessException("Endpoint de telemetria é obrigatório.");
        }
        if (metodoHttp == null || metodoHttp.isBlank()) {
            throw new BusinessException("Método HTTP de telemetria é obrigatório.");
        }
        if (statusHttp < 100 || statusHttp > 599) {
            throw new BusinessException("Status HTTP inválido na telemetria: " + statusHttp);
        }
        if (duracaoMs < 0) {
            throw new BusinessException("Duração da requisição (ms) não pode ser negativa.");
        }
    }

    // =============================================================
    //                     RELATÓRIO DE TELEMETRIA
    // =============================================================
    @Transactional
    public TelemetriaResponseDTO gerarRelatorio() {

        LOG.infof("Gerando relatório de telemetria");

        List<Object[]> results = telemetriaRepository.getEntityManager()
                .createQuery(
                        "select t.metodoHttp, t.endpoint, count(t), avg(t.duracaoMs), " +
                                "min(t.dataRegistro), max(t.dataRegistro) " +
                                "from TelemetriaRegistro t " +
                                "group by t.metodoHttp, t.endpoint",
                        Object[].class
                )
                .getResultList();

        List<TelemetriaServicoDTO> servicos = new ArrayList<>();
        LocalDate inicio = null;
        LocalDate fim = null;

        for (Object[] row : results) {
            String metodo = (String) row[0];
            String endpoint = (String) row[1];
            long qtdChamadas = (Long) row[2];
            Double avgDuracao = (Double) row[3];
            LocalDateTime primeira = (LocalDateTime) row[4];
            LocalDateTime ultima = (LocalDateTime) row[5];

            String nomeServico = metodo + " " + endpoint;

            if (primeira != null) {
                LocalDate d = primeira.toLocalDate();
                if (inicio == null || d.isBefore(inicio)) {
                    inicio = d;
                }
            }

            if (ultima != null) {
                LocalDate d = ultima.toLocalDate();
                if (fim == null || d.isAfter(fim)) {
                    fim = d;
                }
            }

            servicos.add(
                    TelemetriaServicoDTO.builder()
                            .nome(nomeServico)
                            .quantidadeChamadas(qtdChamadas)
                            .mediaTempoRespostaMs(avgDuracao != null ? avgDuracao.longValue() : 0L)
                            .build()
            );
        }

        if (inicio == null || fim == null) {
            LocalDate hoje = LocalDate.now();
            inicio = hoje;
            fim = hoje;
        }

        TelemetriaPeriodoDTO periodo = TelemetriaPeriodoDTO.builder()
                .inicio(inicio)
                .fim(fim)
                .build();

        TelemetriaResponseDTO response = TelemetriaResponseDTO.builder()
                .servicos(servicos)
                .periodo(periodo)
                .build();

        LOG.infof("Relatório de telemetria gerado | servicos=%d | periodo=%s até %s",
                servicos.size(), inicio, fim);

        return response;
    }
}
