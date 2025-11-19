package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.AuthRequestDTO;
import br.com.caixaverso.invest.application.dto.AuthResponseDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class AuthUseCaseTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.AuthUseCase"
        );

        assertTrue(clazz.isInterface(), "AuthUseCase deve ser uma interface");
    }

    @Test
    void testMethodSignature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.AuthUseCase"
        );

        Method method = clazz.getMethod("autenticar", AuthRequestDTO.class);

        assertEquals(AuthResponseDTO.class, method.getReturnType());
    }
}

