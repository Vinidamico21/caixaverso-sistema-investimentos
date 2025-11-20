package br.com.caixaverso.invest.infra.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    void deveConstruirValidationExceptionComStatusBadRequest() {
        String code = "VAL-001";
        String msg = "Campo obrigatório não informado";

        ValidationException ex = new ValidationException(code, msg);

        // herda de ApiException
        assertInstanceOf(ApiException.class, ex);

        // status sempre BAD_REQUEST (400)
        assertEquals(Response.Status.BAD_REQUEST, ex.getStatus());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getStatus().getStatusCode());

        // código de erro e mensagem devem ser preservados
        assertEquals(code, ex.getError());
        assertEquals(msg, ex.getMessage());
    }
}

