package br.com.caixaverso.invest.infra.telemetria;

import br.com.caixaverso.invest.application.dto.TelemetriaResponseDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaServicoDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaPeriodoDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.Map;

@ApplicationScoped
public class RequestMetricsService {

    private static class Stats {
        final LongAdder totalChamadas = new LongAdder();
        final LongAdder somaDuracaoMs = new LongAdder();
    }

    private final Map<String, Stats> statsPorServico = new ConcurrentHashMap<>();

    private volatile Instant primeiraChamada;
    private volatile Instant ultimaChamada;

    public void registrarChamada(String nomeServico, long duracaoMs) {
        if (nomeServico == null || nomeServico.isBlank()) {
            nomeServico = "desconhecido";
        }

        Stats stats = statsPorServico.computeIfAbsent(nomeServico, k -> new Stats());
        stats.totalChamadas.increment();
        stats.somaDuracaoMs.add(duracaoMs);

        Instant agora = Instant.now();
        if (primeiraChamada == null) {
            primeiraChamada = agora;
        }
        ultimaChamada = agora;
    }

    public TelemetriaResponseDTO gerarRelatorio() {
        List<TelemetriaServicoDTO> servicos = new ArrayList<>();

        for (Map.Entry<String, Stats> entry : statsPorServico.entrySet()) {
            String nome = entry.getKey();
            Stats s = entry.getValue();

            long qtd = s.totalChamadas.sum();
            long soma = s.somaDuracaoMs.sum();
            long media = (qtd == 0) ? 0L : soma / qtd;

            TelemetriaServicoDTO dto = TelemetriaServicoDTO.builder()
                    .nome(nome)
                    .quantidadeChamadas(qtd)
                    .mediaTempoRespostaMs(media)
                    .build();

            servicos.add(dto);
        }

        LocalDate inicio = primeiraChamada != null
                ? LocalDate.ofInstant(primeiraChamada, ZoneOffset.UTC)
                : LocalDate.now(ZoneOffset.UTC);

        LocalDate fim = ultimaChamada != null
                ? LocalDate.ofInstant(ultimaChamada, ZoneOffset.UTC)
                : LocalDate.now(ZoneOffset.UTC);

        TelemetriaPeriodoDTO periodo = TelemetriaPeriodoDTO.builder()
                .inicio(inicio)
                .fim(fim)
                .build();

        return TelemetriaResponseDTO.builder()
                .servicos(servicos)
                .periodo(periodo)
                .build();
    }
}
