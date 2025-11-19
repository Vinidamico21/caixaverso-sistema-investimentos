package br.com.seuempresa.invest.application.service;

import br.com.caixaverso.invest.application.dto.TelemetriaResponseDTO;
import br.com.caixaverso.invest.application.port.out.TelemetriaPort;
import br.com.caixaverso.invest.application.service.TelemetriaService;
import br.com.caixaverso.invest.domain.model.TelemetriaRegistro;
import br.com.caixaverso.invest.infra.exception.BusinessException;
import br.com.caixaverso.invest.infra.repository.TelemetriaRegistroRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class TelemetriaServiceTest {

    @Inject
    TelemetriaService service;

    @InjectMock
    TelemetriaPort telemetriaPort;

    @InjectMock
    TelemetriaRegistroRepository telemetriaRepository;

    // Mocks simples (não CDI) criados manualmente
    EntityManager entityManager;
    TypedQuery<Object[]> typedQuery;

    @BeforeEach
    void setup() {
        entityManager = mock(EntityManager.class);
        typedQuery = mock(TypedQuery.class);
        when(telemetriaRepository.getEntityManager()).thenReturn(entityManager);
    }

    // ---------------------------------------------------------
    // REGISTRAR TELEMETRIA
    // ---------------------------------------------------------
    @Test
    void deveRegistrarTelemetriaComSucesso() {
        TelemetriaRegistro reg = TelemetriaRegistro.builder().build();

        doAnswer(inv -> null).when(telemetriaPort).salvar(any(TelemetriaRegistro.class));

        service.registrar(1L, "/api/teste", "GET", true, 200, 150);

        verify(telemetriaPort, times(1)).salvar(any(TelemetriaRegistro.class));
        verifyNoMoreInteractions(telemetriaPort);
    }

    @Test
    void deveFalharQuandoEndpointVazio() {
        assertThrows(BusinessException.class,
                () -> service.registrar(1L, " ", "GET", true, 200, 100));
    }

    @Test
    void deveFalharQuandoMetodoHttpVazio() {
        assertThrows(BusinessException.class,
                () -> service.registrar(1L, "/api/x", " ", true, 200, 100));
    }

    @Test
    void deveFalharQuandoStatusHttpInvalido() {
        assertThrows(BusinessException.class,
                () -> service.registrar(1L, "/api/x", "GET", true, 99, 100));
    }

    @Test
    void deveFalharQuandoDuracaoNegativa() {
        assertThrows(BusinessException.class,
                () -> service.registrar(1L, "/api/x", "GET", true, 200, -5));
    }

    // ---------------------------------------------------------
    // GERAR RELATÓRIO — COM DADOS
    // ---------------------------------------------------------
    @Test
    void deveGerarRelatorioComDados() {
        when(entityManager.createQuery(anyString(), eq(Object[].class)))
                .thenReturn(typedQuery);

        LocalDateTime d1 = LocalDateTime.of(2025, 1, 10, 10, 0);
        LocalDateTime d2 = LocalDateTime.of(2025, 1, 20, 16, 0);

        List<Object[]> rows = List.<Object[]>of(
                new Object[]{"GET", "/api/teste", 5L, 120.0, d1, d2}
        );

        when(typedQuery.getResultList()).thenReturn(rows);

        TelemetriaResponseDTO resp = service.gerarRelatorio();

        assertNotNull(resp);
        assertEquals(1, resp.getServicos().size());
        assertEquals("GET /api/teste", resp.getServicos().get(0).getNome());
        assertEquals(5L, resp.getServicos().get(0).getQuantidadeChamadas());
        assertEquals(120L, resp.getServicos().get(0).getMediaTempoRespostaMs());

        assertEquals(LocalDate.of(2025, 1, 10), resp.getPeriodo().getInicio());
        assertEquals(LocalDate.of(2025, 1, 20), resp.getPeriodo().getFim());
    }

    // ---------------------------------------------------------
    // GERAR RELATÓRIO — SEM DADOS (usa data atual)
    // ---------------------------------------------------------
    @Test
    void deveGerarRelatorioSemDadosUsandoDataDeHoje() {
        when(entityManager.createQuery(anyString(), eq(Object[].class)))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());

        TelemetriaResponseDTO resp = service.gerarRelatorio();

        assertNotNull(resp);
        assertEquals(0, resp.getServicos().size());

        LocalDate hoje = LocalDate.now();
        assertEquals(hoje, resp.getPeriodo().getInicio());
        assertEquals(hoje, resp.getPeriodo().getFim());

        verify(entityManager).createQuery(anyString(), eq(Object[].class));
        verify(typedQuery).getResultList();
        verifyNoMoreInteractions(entityManager, typedQuery);
    }

    // ---------------------------------------------------------
    // GERAR RELATÓRIO — LINHA COM VALORES NULOS
    // ---------------------------------------------------------
    @Test
    void deveGerarRelatorioMesmoComValoresNulos() {
        when(entityManager.createQuery(anyString(), eq(Object[].class)))
                .thenReturn(typedQuery);

        List<Object[]> rows = List.<Object[]>of(
                new Object[]{"POST", "/api/nulo", 2L, null, null, null}
        );

        when(typedQuery.getResultList()).thenReturn(rows);

        TelemetriaResponseDTO resp = service.gerarRelatorio();

        assertNotNull(resp);
        assertEquals(1, resp.getServicos().size());
        assertEquals(0L, resp.getServicos().get(0).getMediaTempoRespostaMs());

        LocalDate hoje = LocalDate.now();
        assertEquals(hoje, resp.getPeriodo().getInicio());
        assertEquals(hoje, resp.getPeriodo().getFim());

        verify(entityManager).createQuery(anyString(), eq(Object[].class));
        verify(typedQuery).getResultList();
        verifyNoMoreInteractions(entityManager, typedQuery);
    }
}
