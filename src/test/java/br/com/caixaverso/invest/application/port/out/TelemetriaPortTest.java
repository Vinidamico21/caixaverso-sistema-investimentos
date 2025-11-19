package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.domain.model.TelemetriaRegistro;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TelemetriaPortTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.TelemetriaPort"
        );

        assertTrue(clazz.isInterface(),
                "TelemetriaPort deve ser uma interface");
    }

    @Test
    void testMethod_salvar_Signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.TelemetriaPort"
        );

        Method method = clazz.getMethod("salvar", TelemetriaRegistro.class);

        assertEquals(TelemetriaRegistro.class, method.getReturnType(),
                "O método salvar deve retornar TelemetriaRegistro");

        assertEquals(1, method.getParameterCount(),
                "O método salvar deve possuir exatamente 1 parâmetro");

        assertEquals(TelemetriaRegistro.class, method.getParameterTypes()[0],
                "O parâmetro deve ser do tipo TelemetriaRegistro");
    }
}

