package br.com.caixaverso.invest.infra.telemetria;

import br.com.caixaverso.invest.application.service.TelemetriaService;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.USER)
@ApplicationScoped
public class RequestMetricsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String START_TIME_KEY = "request-start-time";

    @Inject
    TelemetriaService telemetriaService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        requestContext.setProperty(START_TIME_KEY, System.nanoTime());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        Object startObj = requestContext.getProperty(START_TIME_KEY);
        if (!(startObj instanceof Long)) {
            return;
        }

        long startTime = (Long) startObj;
        long duracaoNs = System.nanoTime() - startTime;
        int duracaoMs = (int) (duracaoNs / 1_000_000L);

        String metodo = requestContext.getMethod();
        String path = "/" + requestContext.getUriInfo().getPath();

        int status = responseContext.getStatus();
        boolean sucesso = status >= 200 && status < 300;

        Long clienteId = null;

        telemetriaService.registrar(
                clienteId,
                path,
                metodo,
                sucesso,
                status,
                duracaoMs
        );
    }
}
