package br.com.seuempresa.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.SimulacaoResumoDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListarSimulacoesUseCaseTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.ListarSimulacoesUseCase"
        );

        assertTrue(clazz.isInterface(),
                "ListarSimulacoesUseCase deve ser uma interface");
    }

    @Test
    void testMethodSignature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.ListarSimulacoesUseCase"
        );

        Method method = clazz.getMethod("listarSimulacoes", Long.class);

        assertEquals(List.class, method.getReturnType(),
                "O método listarSimulacoes deve retornar List");
        assertEquals(1, method.getParameterCount(),
                "O método deve receber exatamente 1 parâmetro");
        assertEquals(Long.class, method.getParameters()[0].getType(),
                "O parâmetro deve ser do tipo Long");
    }
}

