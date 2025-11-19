package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.domain.model.PerfilRiscoRegra;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PerfilRiscoRegraPortTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.PerfilRiscoRegraPort"
        );

        assertTrue(clazz.isInterface(),
                "PerfilRiscoRegraPort deve ser uma interface");
    }

    @Test
    void testMethodSignature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.PerfilRiscoRegraPort"
        );

        Method method = clazz.getMethod("buscarRegraPorScore", int.class);

        assertEquals(Optional.class, method.getReturnType(),
                "buscarRegraPorScore deve retornar Optional<PerfilRiscoRegra>");

        assertEquals(1, method.getParameterCount(),
                "O método deve possuir exatamente 1 parâmetro");

        assertEquals(int.class, method.getParameterTypes()[0],
                "O parâmetro deve ser do tipo int");
    }
}
