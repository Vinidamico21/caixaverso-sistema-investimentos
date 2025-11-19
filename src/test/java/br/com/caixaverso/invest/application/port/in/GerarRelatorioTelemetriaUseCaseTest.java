package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.TelemetriaResponseDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class GerarRelatorioTelemetriaUseCaseTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.GerarRelatorioTelemetriaUseCase"
        );

        assertTrue(clazz.isInterface(),
                "GerarRelatorioTelemetriaUseCase deve ser uma interface");
    }

    @Test
    void testMethodSignature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.in.GerarRelatorioTelemetriaUseCase"
        );

        Method method = clazz.getMethod("gerarRelatorio");

        assertEquals(TelemetriaResponseDTO.class, method.getReturnType(),
                "O método gerarRelatorio deve retornar TelemetriaResponseDTO");
    }
}

