package br.com.seuempresa.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.SimularInvestimentoRequest;
import br.com.caixaverso.invest.application.dto.SimularInvestimentoResponse;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class SimularInvestimentoUseCaseTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.SimularInvestimentoUseCase"
        );

        assertTrue(clazz.isInterface(),
                "SimularInvestimentoUseCase deve ser uma interface");
    }

    @Test
    void testMethodSignature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.SimularInvestimentoUseCase"
        );

        Method method = clazz.getMethod(
                "executarSimulacao",
                SimularInvestimentoRequest.class
        );

        assertEquals(SimularInvestimentoResponse.class, method.getReturnType(),
                "O método deve retornar SimularInvestimentoResponse");

        assertEquals(1, method.getParameterCount(),
                "O método deve possuir exatamente 1 parâmetro");

        assertEquals(SimularInvestimentoRequest.class, method.getParameters()[0].getType(),
                "O parâmetro deve ser do tipo SimularInvestimentoRequest");
    }
}
