package br.com.seuempresa.invest.application.port.out;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoRiscoRegraPortTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.ProdutoRiscoRegraPort"
        );

        assertTrue(clazz.isInterface(),
                "ProdutoRiscoRegraPort deve ser uma interface");
    }

    @Test
    void testMethod_classificar_Signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.ProdutoRiscoRegraPort"
        );

        Method method = clazz.getMethod("classificar", BigDecimal.class);

        assertEquals(String.class, method.getReturnType(),
                "O método classificar deve retornar String (risco)");

        assertEquals(1, method.getParameterCount(),
                "O método classificar deve possuir exatamente 1 parâmetro");

        assertEquals(BigDecimal.class, method.getParameterTypes()[0],
                "O parâmetro deve ser do tipo BigDecimal (taxaAnual)");
    }
}

