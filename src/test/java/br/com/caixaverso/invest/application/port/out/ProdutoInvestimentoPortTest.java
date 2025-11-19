package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.domain.model.ProdutoInvestimento;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoInvestimentoPortTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.ProdutoInvestimentoPort"
        );

        assertTrue(clazz.isInterface(),
                "ProdutoInvestimentoPort deve ser uma interface");
    }

    @Test
    void testMethod_findAll_Signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.ProdutoInvestimentoPort"
        );

        Method method = clazz.getMethod("findAll");

        assertEquals(List.class, method.getReturnType(),
                "findAll deve retornar List<ProdutoInvestimento>");

        assertEquals(0, method.getParameterCount(),
                "findAll não deve possuir parâmetros");
    }

    @Test
    void testMethod_findByTipo_Signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.ProdutoInvestimentoPort"
        );

        Method method = clazz.getMethod("findByTipo", String.class);

        assertEquals(List.class, method.getReturnType(),
                "findByTipo deve retornar List<ProdutoInvestimento>");

        assertEquals(1, method.getParameterCount(),
                "findByTipo deve possuir exatamente 1 parâmetro");

        assertEquals(String.class, method.getParameterTypes()[0],
                "O parâmetro deve ser do tipo String");
    }
}

