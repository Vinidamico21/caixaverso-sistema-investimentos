package br.com.caixaverso.invest.infra.exception;

import jakarta.ws.rs.core.Response;

public class ValidationException extends ApiException {

    public ValidationException(String code, String message) {
        super(Response.Status.BAD_REQUEST, code, message); // 400
    }
}
