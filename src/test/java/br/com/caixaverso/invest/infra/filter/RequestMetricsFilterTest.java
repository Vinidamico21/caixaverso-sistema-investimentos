package br.com.caixaverso.invest.infra.filter;

import br.com.caixaverso.invest.application.service.TelemetriaService;
import br.com.caixaverso.invest.infra.telemetria.RequestMetricsFilter;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RequestMetricsFilterTest {

    @InjectMocks
    RequestMetricsFilter filter;

    @Mock
    TelemetriaService telemetriaService;

    @Mock
    ContainerRequestContext requestContext;

    @Mock
    ContainerResponseContext responseContext;

    @Mock
    UriInfo uriInfo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // TESTE COMPLETO DE REGISTRO COM NORMALIZAÇÃO
    @Test
    void deveRegistrarComEndpointNormalizado() {

        when(requestContext.getProperty("request-start-time"))
                .thenReturn(System.nanoTime() - 5_000_000L);

        when(requestContext.getMethod()).thenReturn("GET");

        when(uriInfo.getPath())
                .thenReturn("api/v1/produtos-recomendados/CONSERVADOR/123");
        when(requestContext.getUriInfo()).thenReturn(uriInfo);

        when(responseContext.getStatus()).thenReturn(200);

        filter.filter(requestContext, responseContext);

        verify(telemetriaService).registrar(
                isNull(),
                eq("/api/v1/produtos-recomendados/{perfil}/{id}"),
                eq("GET"),
                eq(true),
                eq(200),
                anyInt()
        );
    }

    // TESTE NORMALIZAÇÃO DE UUID
    @Test
    void deveNormalizarUUID() {

        when(requestContext.getProperty("request-start-time"))
                .thenReturn(System.nanoTime() - 1_000_000L);

        when(requestContext.getMethod()).thenReturn("GET");

        when(uriInfo.getPath()).thenReturn("api/v1/cliente/550e8400-e29b-41d4-a716-446655440000");
        when(requestContext.getUriInfo()).thenReturn(uriInfo);

        when(responseContext.getStatus()).thenReturn(200);

        filter.filter(requestContext, responseContext);

        verify(telemetriaService).registrar(
                any(),
                eq("/api/v1/cliente/{uuid}"),
                eq("GET"),
                eq(true),
                eq(200),
                anyInt()
        );
    }

    // TESTE NORMALIZAÇÃO DE BARRAS DUPLAS
    @Test
    void deveNormalizarBarrasDuplas() {

        when(requestContext.getProperty("request-start-time"))
                .thenReturn(System.nanoTime() - 1_000_000L);

        when(requestContext.getMethod()).thenReturn("POST");

        when(uriInfo.getPath()).thenReturn("//api//v1//login//");
        when(requestContext.getUriInfo()).thenReturn(uriInfo);

        when(responseContext.getStatus()).thenReturn(201);

        filter.filter(requestContext, responseContext);

        verify(telemetriaService).registrar(
                any(),
                eq("/api/v1/login"),
                eq("POST"),
                eq(true),
                eq(201),
                anyInt()
        );
    }

    // QUANDO NÃO EXISTE START_TIME → NÃO REGISTRA
    @Test
    void naoDeveRegistrarQuandoStartTimeNaoExiste() {
        when(requestContext.getProperty("request-start-time")).thenReturn(null);

        filter.filter(requestContext, responseContext);

        verifyNoInteractions(telemetriaService);
    }

    // STATUS DE ERRO → sucesso=false
    @Test
    void deveRegistrarErroHttp() {

        when(requestContext.getProperty("request-start-time"))
                .thenReturn(System.nanoTime() - 2_000_000L);

        when(requestContext.getMethod()).thenReturn("GET");

        when(uriInfo.getPath()).thenReturn("api/v1/produtos/999");
        when(requestContext.getUriInfo()).thenReturn(uriInfo);

        when(responseContext.getStatus()).thenReturn(500);

        filter.filter(requestContext, responseContext);

        verify(telemetriaService).registrar(
                any(),
                eq("/api/v1/produtos/{id}"),
                eq("GET"),
                eq(false),
                eq(500),
                anyInt()
        );
    }

    // TESTE DO MÉTODO QUE SALVA START_TIME (filter request)
    @Test
    void deveSalvarStartTime() {
        filter.filter(requestContext);

        verify(requestContext).setProperty(eq("request-start-time"), anyLong());
    }
}
