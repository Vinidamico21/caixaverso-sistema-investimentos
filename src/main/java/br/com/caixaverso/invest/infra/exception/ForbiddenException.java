package br.com.caixaverso.invest.infra.exception;

import jakarta.ws.rs.core.Response;

public class ForbiddenException extends ApiException {

    public ForbiddenException(String code, String message) {
        super(Response.Status.FORBIDDEN, code, message); // 403
    }
}
