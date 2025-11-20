package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.TelemetriaPeriodoDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaResponseDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaServicoDTO;
import br.com.caixaverso.invest.application.port.out.TelemetriaPort;
import br.com.caixaverso.invest.infra.persistence.entity.TelemetriaRegistro;
import br.com.caixaverso.invest.infra.exception.BusinessException;
import br.com.caixaverso.invest.infra.repository.TelemetriaRegistroRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelemetriaServiceTest {

    @InjectMocks
    TelemetriaService service;

    @Mock
    TelemetriaPort telemetriaPort;

    @Mock
    TelemetriaRegistroRepository telemetriaRepository;

    @Mock
    EntityManager entityManager;

    @Mock
    TypedQuery<Object[]> typedQuery;

    // ----------------------------------------------------
    // registrar() - fluxo feliz
    // ----------------------------------------------------
    @Test
    void deveRegistrarTelemetriaComSucessoQuandoDadosValidos() {
        when(telemetriaPort.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        service.registrar(
                1L,
                "/api/simulacoes",
                "GET",
                true,
                200,
                150
        );

        verify(telemetriaPort, times(1)).salvar(any(TelemetriaRegistro.class));
        verifyNoMoreInteractions(telemetriaPort);
    }

    // ----------------------------------------------------
    // validarRegistro() - endpoint inválido
    // ----------------------------------------------------
    @Test
    void deveFalharQuandoEndpointForNuloOuEmBranco() {
        assertThrows(BusinessException.class,
                () -> service.registrar(1L, null, "GET", true, 200, 10));

        assertThrows(BusinessException.class,
                () -> service.registrar(1L, "   ", "GET", true, 200, 10));

        verifyNoInteractions(telemetriaPort);
    }

    // ----------------------------------------------------
    // validarRegistro() - método HTTP inválido
    // ----------------------------------------------------
    @Test
    void deveFalharQuandoMetodoHttpForNuloOuEmBranco() {
        assertThrows(BusinessException.class,
                () -> service.registrar(1L, "/api/test", null, true, 200, 10));

        assertThrows(BusinessException.class,
                () -> service.registrar(1L, "/api/test", "   ", true, 200, 10));

        verifyNoInteractions(telemetriaPort);
    }

    // ----------------------------------------------------
    // validarRegistro() - status HTTP inválido (fora de 100-599)
    // ----------------------------------------------------
    @Test
    void deveFalharQuandoStatusHttpForInvalido() {
        assertThrows(BusinessException.class,
                () -> service.registrar(1L, "/api/test", "GET", true, 99, 10));

        assertThrows(BusinessException.class,
                () -> service.registrar(1L, "/api/test", "GET", true, 600, 10));

        verifyNoInteractions(telemetriaPort);
    }

    // ----------------------------------------------------
    // validarRegistro() - duração negativa
    // ----------------------------------------------------
    @Test
    void deveFalharQuandoDuracaoForNegativa() {
        assertThrows(BusinessException.class,
                () -> service.registrar(1L, "/api/test", "GET", true, 200, -1));

        verifyNoInteractions(telemetriaPort);
    }

    // ----------------------------------------------------
    // gerarRelatorio() - com resultados agregados
    // ----------------------------------------------------
    @Test
    void deveGerarRelatorioDeTelemetriaComResultados() {
        when(telemetriaRepository.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(anyString(), eq(Object[].class))).thenReturn(typedQuery);

        LocalDateTime primeira1 = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime ultima1   = LocalDateTime.of(2024, 1, 3, 11, 0);

        // linha 1: endpoint correto
        Object[] row1 = new Object[] {
                "/api/simulacoes",   // endpoint → row[0]
                3L,                  // count → row[1]
                150.0,               // avg → row[2]
                primeira1,           // min → row[3]
                ultima1              // max → row[4]
        };

        // linha 2: média nula + datas nulas
        Object[] row2 = new Object[] {
                "/api/clientes",
                1L,
                null,
                null,
                null
        };

        when(typedQuery.getResultList()).thenReturn(List.of(row1, row2));

        TelemetriaResponseDTO resp = service.gerarRelatorio();

        assertNotNull(resp);
        assertNotNull(resp.getPeriodo());
        assertNotNull(resp.getServicos());
        assertEquals(2, resp.getServicos().size());

        TelemetriaPeriodoDTO periodo = resp.getPeriodo();

        // período deve ser o menor e o maior encontrados
        assertEquals(primeira1.toLocalDate(), periodo.getInicio());
        assertEquals(ultima1.toLocalDate(), periodo.getFim());

        // serviço 1
        TelemetriaServicoDTO s1 = resp.getServicos().stream()
                .filter(s -> "/api/simulacoes".equals(s.getNome()))
                .findFirst()
                .orElseThrow();

        assertEquals(3L, s1.getQuantidadeChamadas());
        assertEquals(150L, s1.getMediaTempoRespostaMs());

        // serviço 2
        TelemetriaServicoDTO s2 = resp.getServicos().stream()
                .filter(s -> "/api/clientes".equals(s.getNome()))
                .findFirst()
                .orElseThrow();

        assertEquals(1L, s2.getQuantidadeChamadas());
        assertEquals(0L, s2.getMediaTempoRespostaMs()); // média nula → 0L
    }

    // ----------------------------------------------------
    // gerarRelatorio() - sem resultados (lista vazia)
    // ----------------------------------------------------
    @Test
    void deveGerarRelatorioComPeriodoHojeQuandoNaoHouverRegistros() {
        when(telemetriaRepository.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(anyString(), eq(Object[].class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        TelemetriaResponseDTO resp = service.gerarRelatorio();

        assertNotNull(resp);
        assertNotNull(resp.getServicos());
        assertTrue(resp.getServicos().isEmpty());

        TelemetriaPeriodoDTO periodo = resp.getPeriodo();
        assertNotNull(periodo);
        assertNotNull(periodo.getInicio());
        assertNotNull(periodo.getFim());
        // quando não há registros, inicio e fim são hoje (iguais)
        assertEquals(periodo.getInicio(), periodo.getFim());
    }
}
