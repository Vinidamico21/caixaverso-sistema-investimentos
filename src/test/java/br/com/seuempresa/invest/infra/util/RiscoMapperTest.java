package br.com.seuempresa.invest.infra.util;

import br.com.caixaverso.invest.infra.util.RiscoMapper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class RiscoMapperTest {

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<RiscoMapper> constructor = RiscoMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertDoesNotThrow(() -> {
            try {
                constructor.newInstance();
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void deveMapearConservadorParaBaixo() {
        assertEquals("Baixo", RiscoMapper.mapearRiscoHumano("CONSERVADOR"));
    }

    @Test
    void deveMapearModeradoParaMedio() {
        assertEquals("Médio", RiscoMapper.mapearRiscoHumano("MODERADO"));
    }

    @Test
    void deveMapearAgressivoParaAlto() {
        assertEquals("Alto", RiscoMapper.mapearRiscoHumano("AGRESSIVO"));
    }

    @Test
    void deveRetornarDesconhecidoParaNulo() {
        assertEquals("Desconhecido", RiscoMapper.mapearRiscoHumano(null));
    }

    @Test
    void deveRetornarDesconhecidoParaValorInvalido() {
        assertEquals("Desconhecido", RiscoMapper.mapearRiscoHumano("RISCO_X"));
    }

    @Test
    void deveSerCaseInsensitive() {
        assertEquals("Baixo", RiscoMapper.mapearRiscoHumano("conservador"));
        assertEquals("Médio", RiscoMapper.mapearRiscoHumano("Moderado"));
        assertEquals("Alto",  RiscoMapper.mapearRiscoHumano("AgReSsIvO"));
    }
}
