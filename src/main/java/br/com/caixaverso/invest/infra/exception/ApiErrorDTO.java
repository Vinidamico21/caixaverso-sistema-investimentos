 package br.com.caixaverso.invest.infra.exception;

public record ApiErrorDTO(
        int status,
        String error,
        String message,
        String path
) {}
