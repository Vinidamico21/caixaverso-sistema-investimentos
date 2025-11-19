package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.response.PageResponse;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

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

        // método agora: PageResponse<InvestimentoResponseDTO> listarPorCliente(String clienteIdRaw, int page, int size)
        Method method = clazz.getMethod("listarPorCliente", String.class, int.class, int.class);

        assertEquals(PageResponse.class, method.getReturnType(),
                "O método listarPorCliente deve retornar PageResponse");
        assertEquals(3, method.getParameterCount(),
                "O método deve ter exatamente 3 parâmetros");

        Class<?>[] paramTypes = method.getParameterTypes();
        assertEquals(String.class, paramTypes[0], "Primeiro parâmetro deve ser String (clienteIdRaw)");
        assertEquals(int.class, paramTypes[1], "Segundo parâmetro deve ser int (page)");
        assertEquals(int.class, paramTypes[2], "Terceiro parâmetro deve ser int (size)");
    }
}
