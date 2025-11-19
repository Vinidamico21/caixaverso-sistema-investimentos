package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.PerfilRiscoResponseDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CalcularPerfilRiscoUseCaseTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.CalcularPerfilRiscoUseCase"
        );

        assertTrue(clazz.isInterface(), "CalcularPerfilRiscoUseCase deve ser uma interface");
    }

    @Test
    void testMethodSignature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.CalcularPerfilRiscoUseCase"
        );

        Method method = clazz.getMethod("calcularPerfil", Long.class);

        assertEquals(PerfilRiscoResponseDTO.class, method.getReturnType());
    }
}

