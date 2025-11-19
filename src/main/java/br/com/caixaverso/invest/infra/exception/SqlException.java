package br.com.caixaverso.invest.infra.exception;

import jakarta.ws.rs.core.Response;

public class SqlException extends ApiException {

    public SqlException(String message) {
        super(Response.Status.INTERNAL_SERVER_ERROR, "DATABASE_ERROR", message);
    }
}