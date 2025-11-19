package br.com.caixaverso.invest.application.port.out;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class FrequenciaInvestRegraPortTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.FrequenciaInvestRegraPort"
        );

        assertTrue(clazz.isInterface(),
                "FrequenciaInvestRegraPort deve ser uma interface");
    }

    @Test
    void testMethodSignature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.FrequenciaInvestRegraPort"
        );

        Method method = clazz.getMethod("buscarPontuacao", int.class);

        assertEquals(int.class, method.getReturnType(),
                "O método buscarPontuacao deve retornar int");

        assertEquals(1, method.getParameterCount(),
                "O método deve ter exatamente 1 parâmetro");

        assertEquals(int.class, method.getParameterTypes()[0],
                "O parâmetro deve ser do tipo int");
    }
}

