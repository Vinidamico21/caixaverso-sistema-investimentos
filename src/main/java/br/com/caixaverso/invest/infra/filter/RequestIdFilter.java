package br.com.caixaverso.invest.infra.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.MDC;

import java.io.IOException;
import java.util.UUID;

@Provider
public class RequestIdFilter implements ContainerRequestFilter {

    private static final String REQUEST_ID = "requestId";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String id = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID, id);
    }
}
