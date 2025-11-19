package br.com.caixaverso.invest.infra.telemetria;

import br.com.caixaverso.invest.application.dto.TelemetriaPeriodoDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaResponseDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaServicoDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestMetricsServiceTest {

    // ----------------------------------------------------
    // 1) Fluxo normal: várias chamadas, mesma key
    // ----------------------------------------------------
    @Test
    void deveRegistrarChamadasECalcularMediaPorServico() {
        RequestMetricsService service = new RequestMetricsService();

        // 2 chamadas para o mesmo serviço
        service.registrarChamada("simulacao-investimento", 100L);
        service.registrarChamada("simulacao-investimento", 300L);

        TelemetriaResponseDTO relatorio = service.gerarRelatorio();

        assertNotNull(relatorio);
        assertNotNull(relatorio.getPeriodo());

        TelemetriaPeriodoDTO periodo = relatorio.getPeriodo();
        assertNotNull(periodo.getInicio());
        assertNotNull(periodo.getFim());
        // início não deve ser depois do fim
        assertFalse(periodo.getInicio().isAfter(periodo.getFim()));

        List<TelemetriaServicoDTO> servicos = relatorio.getServicos();
        assertFalse(servicos.isEmpty());

        TelemetriaServicoDTO simulacao = servicos.stream()
                .filter(s -> "simulacao-investimento".equals(s.getNome()))
                .findFirst()
                .orElseThrow();

        assertEquals(2L, simulacao.getQuantidadeChamadas());
        // média entre 100 e 300 = 200
        assertEquals(200L, simulacao.getMediaTempoRespostaMs());
    }

    // ----------------------------------------------------
    // 2) Nome null / em branco → "desconhecido"
    // ----------------------------------------------------
    @Test
    void deveUsarNomeDesconhecidoQuandoNomeForNuloOuEmBranco() {
        RequestMetricsService service = new RequestMetricsService();

        service.registrarChamada(null, 100L);   // vira "desconhecido"
        service.registrarChamada("   ", 300L);  // ainda "desconhecido"

        TelemetriaResponseDTO relatorio = service.gerarRelatorio();

        TelemetriaServicoDTO dto = relatorio.getServicos().stream()
                .filter(s -> "desconhecido".equals(s.getNome()))
                .findFirst()
                .orElseThrow();

        assertEquals(2L, dto.getQuantidadeChamadas());
        assertEquals(200L, dto.getMediaTempoRespostaMs());
    }

    // ----------------------------------------------------
    // 3) Nenhuma chamada registrada → lista vazia, periodo com datas
    // ----------------------------------------------------
    @Test
    void deveGerarRelatorioComListaVaziaQuandoNaoHouverChamadas() {
        RequestMetricsService service = new RequestMetricsService();

        TelemetriaResponseDTO relatorio = service.gerarRelatorio();

        assertNotNull(relatorio);
        assertNotNull(relatorio.getServicos());
        assertTrue(relatorio.getServicos().isEmpty());

        TelemetriaPeriodoDTO periodo = relatorio.getPeriodo();
        assertNotNull(periodo);
        LocalDate inicio = periodo.getInicio();
        LocalDate fim = periodo.getFim();

        assertNotNull(inicio);
        assertNotNull(fim);
        // nesse caso usa LocalDate.now(UTC) para ambos ⇒ no máximo iguais
        assertFalse(inicio.isAfter(fim));
    }

    // ----------------------------------------------------
    // 4) Cobrir ramo qtd == 0 no cálculo da média (usando reflection)
    // ----------------------------------------------------
    @Test
    void deveRetornarMediaZeroQuandoQuantidadeChamadasForZero() throws Exception {
        RequestMetricsService service = new RequestMetricsService();

        // Obter o mapa privado statsPorServico
        Field statsField = RequestMetricsService.class.getDeclaredField("statsPorServico");
        statsField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Object> statsMap = (Map<String, Object>) statsField.get(service);

        // Criar instância da inner class privada Stats
        Class<?> statsClass =
                Class.forName("br.com.caixaverso.invest.infra.telemetria.RequestMetricsService$Stats");
        Constructor<?> constructor = statsClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object statsInstance = constructor.newInstance(); // totalChamadas e somaDuracaoMs = 0

        // Inserir no mapa como se fosse um serviço já monitorado
        statsMap.put("servico-zero", statsInstance);

        TelemetriaResponseDTO relatorio = service.gerarRelatorio();

        TelemetriaServicoDTO dto = relatorio.getServicos().stream()
                .filter(s -> "servico-zero".equals(s.getNome()))
                .findFirst()
                .orElseThrow();

        // como qtd == 0, a média deve ser 0L (ramo do ternário)
        assertEquals(0L, dto.getQuantidadeChamadas());
        assertEquals(0L, dto.getMediaTempoRespostaMs());
    }

}
