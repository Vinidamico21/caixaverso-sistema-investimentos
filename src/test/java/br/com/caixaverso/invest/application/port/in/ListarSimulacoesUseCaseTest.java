package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.PageResponse;
import br.com.caixaverso.invest.application.dto.SimulacaoResumoDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

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

        Method method = clazz.getMethod("listarSimulacoes", Long.class, int.class, int.class);

        // Retorno: PageResponse<SimulacaoResumoDTO>
        assertEquals(PageResponse.class, method.getReturnType(),
                "O método listarSimulacoes deve retornar PageResponse");

        // Parâmetros: (Long clienteId, int page, int size)
        assertEquals(3, method.getParameterCount(),
                "O método deve receber exatamente 3 parâmetros");

        assertEquals(Long.class, method.getParameters()[0].getType(),
                "O primeiro parâmetro deve ser do tipo Long (clienteId)");

        assertEquals(int.class, method.getParameters()[1].getType(),
                "O segundo parâmetro deve ser do tipo int (page)");

        assertEquals(int.class, method.getParameters()[2].getType(),
                "O terceiro parâmetro deve ser do tipo int (size)");
    }
}
