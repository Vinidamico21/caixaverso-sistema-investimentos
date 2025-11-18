 package br.com.caixaverso.invest.infra.exception;

import jakarta.ws.rs.core.Response;

public class BusinessException extends ApiException {

    public BusinessException(String message) {
        super(Response.Status.BAD_REQUEST, "Business Error", message);
    }
}
