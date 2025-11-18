 package br.com.caixaverso.invest.infra.exception;

import jakarta.ws.rs.core.Response;

public class UnauthorizedException extends ApiException {

    public UnauthorizedException(String message) {
        super(Response.Status.UNAUTHORIZED, "Unauthorized", message);
    }
}
