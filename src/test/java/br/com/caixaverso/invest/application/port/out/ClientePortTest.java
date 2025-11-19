package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.domain.model.Cliente;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClientePortTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.ClientePort"
        );

        assertTrue(clazz.isInterface(),
                "ClientePort deve ser uma interface");
    }

    @Test
    void testMethodSignature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.ClientePort"
        );

        Method method = clazz.getMethod("findById", Long.class);

        assertEquals(Optional.class, method.getReturnType(),
                "O método findById deve retornar Optional<Cliente>");

        assertEquals(1, method.getParameterCount(),
                "findById deve possuir exatamente 1 parâmetro");

        assertEquals(Long.class, method.getParameterTypes()[0],
                "O parâmetro deve ser do tipo Long");
    }
}

