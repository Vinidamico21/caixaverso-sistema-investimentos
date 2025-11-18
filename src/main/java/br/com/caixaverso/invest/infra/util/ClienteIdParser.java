package br.com.caixaverso.invest.infra.util;

import br.com.caixaverso.invest.infra.exception.BusinessException;

public final class ClienteIdParser {

    private ClienteIdParser() {
    }

    public static Long parseClienteIdToLong(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new BusinessException("Parâmetro 'clienteId' é obrigatório.");
        }
        try {
            return Long.valueOf(raw);
        } catch (NumberFormatException e) {
            throw new BusinessException("Parâmetro 'clienteId' inválido. Deve ser numérico.");
        }
    }
}
