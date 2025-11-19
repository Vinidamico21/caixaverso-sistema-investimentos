package br.com.seuempresa.invest.application.port.in;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AgruparSimulacoesPorProdutoDiaUseCaseTest {

    @Test
    void testInterfaceExistsAndHasExpectedMethod() throws Exception {
        Class<?> clazz = Class.forName("br.com.caixaverso.invest.application.port.in.AgruparSimulacoesPorProdutoDiaUseCase");

        assertTrue(clazz.isInterface(), "Deveria ser uma interface");

        Method method = clazz.getMethod("agrupamentoPorProdutoDia");

        assertEquals(List.class, method.getReturnType());
    }
}
