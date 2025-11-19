package br.com.caixaverso.invest.infra.util;

import br.com.caixaverso.invest.infra.exception.BusinessException;
import br.com.caixaverso.invest.infra.util.ClienteIdParser;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ClienteIdParserTest {

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<ClienteIdParser> constructor = ClienteIdParser.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // A simples chamada garante cobertura da linha, mesmo que retorne exception
        assertDoesNotThrow(() -> {
            try {
                constructor.newInstance();
            } catch (InvocationTargetException e) {
                // não esperamos instanciar, mas garantir cobertura do construtor
                throw e.getCause();
            }
        });
    }

    @Test
    void deveConverterClienteIdValido() {
        Long result = ClienteIdParser.parseClienteIdToLong("12345");
        assertEquals(12345L, result);
    }

    @Test
    void deveLancarExcecaoQuandoClienteIdForNull() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> ClienteIdParser.parseClienteIdToLong(null));

        assertEquals("Parâmetro 'clienteId' é obrigatório.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoClienteIdForBlank() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> ClienteIdParser.parseClienteIdToLong("   "));

        assertEquals("Parâmetro 'clienteId' é obrigatório.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoClienteIdNaoForNumerico() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> ClienteIdParser.parseClienteIdToLong("abc123"));

        assertEquals("Parâmetro 'clienteId' inválido. Deve ser numérico.", ex.getMessage());
    }
}
