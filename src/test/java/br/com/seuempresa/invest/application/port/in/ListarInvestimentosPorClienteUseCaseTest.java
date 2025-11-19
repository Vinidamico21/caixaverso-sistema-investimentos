package br.com.seuempresa.invest.application.port.in;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListarInvestimentosPorClienteUseCaseTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.ListarInvestimentosPorClienteUseCase"
        );

        assertTrue(clazz.isInterface(),
                "ListarInvestimentosPorClienteUseCase deve ser uma interface");
    }

    @Test
    void testMethodSignature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.ListarInvestimentosPorClienteUseCase"
        );

        Method method = clazz.getMethod("listarPorCliente", String.class);

        assertEquals(List.class, method.getReturnType(),
                "O método listarPorCliente deve retornar List");
        assertEquals(1, method.getParameterCount(),
                "O método deve ter exatamente 1 parâmetro");
    }
}

