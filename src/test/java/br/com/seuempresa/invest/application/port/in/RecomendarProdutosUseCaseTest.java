package br.com.seuempresa.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.ProdutoRecomendadoDTO;
import br.com.caixaverso.invest.application.dto.RecomendacaoResponseDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecomendarProdutosUseCaseTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.RecomendarProdutosUseCase"
        );

        assertTrue(clazz.isInterface(),
                "RecomendarProdutosUseCase deve ser uma interface");
    }

    @Test
    void testMethod_recomendarParaCliente_signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.RecomendarProdutosUseCase"
        );

        Method method = clazz.getMethod("recomendarParaCliente", Long.class);

        assertEquals(RecomendacaoResponseDTO.class, method.getReturnType(),
                "O método recomendarParaCliente deve retornar RecomendacaoResponseDTO");
        assertEquals(1, method.getParameterCount());
        assertEquals(Long.class, method.getParameterTypes()[0]);
    }

    @Test
    void testMethod_recomendarPorPerfil_signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.RecomendarProdutosUseCase"
        );

        Method method = clazz.getMethod("recomendarPorPerfil", String.class);

        assertEquals(List.class, method.getReturnType(),
                "O método recomendarPorPerfil deve retornar List<ProdutoRecomendadoDTO>");
        assertEquals(1, method.getParameterCount());
        assertEquals(String.class, method.getParameterTypes()[0]);
    }
}
