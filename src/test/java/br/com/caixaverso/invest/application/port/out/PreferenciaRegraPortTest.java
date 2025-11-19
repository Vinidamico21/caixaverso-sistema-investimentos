package br.com.caixaverso.invest.application.port.out;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class PreferenciaRegraPortTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.PreferenciaRegraPort"
        );

        assertTrue(clazz.isInterface(),
                "PreferenciaRegraPort deve ser uma interface");
    }

    @Test
    void testMethodSignature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.PreferenciaRegraPort"
        );

        Method method = clazz.getMethod("buscarPontuacao", String.class);

        assertEquals(int.class, method.getReturnType(),
                "O método buscarPontuacao deve retornar int");

        assertEquals(1, method.getParameterCount(),
                "buscarPontuacao deve ter exatamente 1 parâmetro");

        assertEquals(String.class, method.getParameterTypes()[0],
                "O parâmetro deve ser do tipo String");
    }
}

