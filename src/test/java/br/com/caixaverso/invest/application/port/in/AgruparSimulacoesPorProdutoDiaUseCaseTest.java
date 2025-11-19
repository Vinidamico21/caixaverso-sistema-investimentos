package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.PageResponse;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class AgruparSimulacoesPorProdutoDiaUseCaseTest {

    @Test
    void testInterfaceExistsAndHasExpectedMethod() throws Exception {
        Class<?> clazz = Class.forName("br.com.caixaverso.invest.application.port.in.AgruparSimulacoesPorProdutoDiaUseCase");

        assertTrue(clazz.isInterface(), "Deveria ser uma interface");

        // novo método paginado: (int page, int size)
        Method method = clazz.getMethod("agrupamentoPorProdutoDia", int.class, int.class);

        assertEquals(PageResponse.class, method.getReturnType(),
                "O método agrupamentoPorProdutoDia deve retornar PageResponse");
    }
}