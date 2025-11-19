package br.com.caixaverso.invest.application.port.in;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class RegistrarTelemetriaUseCaseTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.RegistrarTelemetriaUseCase"
        );

        assertTrue(clazz.isInterface(),
                "RegistrarTelemetriaUseCase deve ser uma interface");
    }

    @Test
    void testMethodSignature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.RegistrarTelemetriaUseCase"
        );

        Method method = clazz.getMethod(
                "registrar",
                Long.class,
                String.class,
                String.class,
                boolean.class,
                int.class,
                int.class
        );

        assertEquals(void.class, method.getReturnType(),
                "O método registrar deve retornar void");

        assertEquals(6, method.getParameterCount(),
                "O método registrar deve ter 6 parâmetros");
    }
}

