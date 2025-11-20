package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.TelemetriaPeriodoDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaResponseDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaServicoDTO;
import br.com.caixaverso.invest.infra.persistence.entity.TelemetriaRegistro;
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

import static br.com.caixaverso.invest.domain.constants.PerfilConstantes.*;

@ApplicationScoped
public class TelemetriaService implements
        GerarRelatorioTelemetriaUseCase,
        RegistrarTelemetriaUseCase {

    private static final Logger LOG = Logger.getLogger(TelemetriaService.class);

    @Inject
    TelemetriaPort telemetriaPort;

    @Inject
    TelemetriaRegistroRepository telemetriaRepository;

    // REGISTRO DE TELEMETRIA
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

        LOG.debugf(LOG_TELEMETRIA_REGISTRANDO,
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
            throw new BusinessException(ERR_TELEMETRIA_ENDPOINT_OBRIGATORIO);
        }
        if (metodoHttp == null || metodoHttp.isBlank()) {
            throw new BusinessException(ERR_TELEMETRIA_METODO_OBRIGATORIO);
        }
        if (statusHttp < 100 || statusHttp > 599) {
            throw new BusinessException(ERR_TELEMETRIA_STATUS_INVALIDO + statusHttp);
        }
        if (duracaoMs < 0) {
            throw new BusinessException(ERR_TELEMETRIA_DURACAO_NEGATIVA);
        }
    }

    // RELATÓRIO DE TELEMETRIA
    @Transactional
    public TelemetriaResponseDTO gerarRelatorio() {

        LOG.info(LOG_TELEMETRIA_RELATORIO_INICIO);

        List<Object[]> results = telemetriaRepository.getEntityManager()
                .createQuery(
                        "select t.endpoint,\n" +
                                "       count(t),\n" +
                                "       avg(t.duracaoMs),\n" +
                                "       min(t.dataRegistro),\n" +
                                "       max(t.dataRegistro)\n" +
                                "from TelemetriaRegistro t\n" +
                                "group by t.endpoint\n",
                        Object[].class
                )
                .getResultList();

        List<TelemetriaServicoDTO> servicos = new ArrayList<>();
        LocalDate inicio = null;
        LocalDate fim = null;

        for (Object[] row : results) {

            String endpoint = (String) row[0];
            long qtdChamadas = (Long) row[1];
            Double avgDuracao = (Double) row[2];
            LocalDateTime primeira = (LocalDateTime) row[3];
            LocalDateTime ultima = (LocalDateTime) row[4];

            String nomeServico = endpoint;

            if (primeira != null) {
                LocalDate d = primeira.toLocalDate();
                if (inicio == null || d.isBefore(inicio)) inicio = d;
            }

            if (ultima != null) {
                LocalDate d = ultima.toLocalDate();
                if (fim == null || d.isAfter(fim)) fim = d;
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

        LOG.infof(LOG_TELEMETRIA_RELATORIO_FIM,
                servicos.size(), inicio, fim);

        return response;
    }
}
