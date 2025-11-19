package br.com.caixaverso.invest.domain.constants;

import br.com.caixaverso.invest.domain.constants.PerfilConstantes;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PerfilConstantesTest {

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<PerfilConstantes> constructor =
                PerfilConstantes.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()),
                "O construtor deve ser privado");

        constructor.setAccessible(true);
        constructor.newInstance(); // apenas para cobertura
    }

    @Test
    void testPerfis() {
        assertEquals("CONSERVADOR", PerfilConstantes.PERFIL_CONSERVADOR);
        assertEquals("MODERADO", PerfilConstantes.PERFIL_MODERADO);
        assertEquals("AGRESSIVO", PerfilConstantes.PERFIL_AGRESSIVO);
        assertEquals("DESCONHECIDO", PerfilConstantes.PERFIL_DESCONHECIDO);
    }

    @Test
    void testPreferencias() {
        assertEquals("LIQUIDEZ", PerfilConstantes.PREF_LIQUIDEZ);
        assertEquals("RENTABILIDADE", PerfilConstantes.PREF_RENTABILIDADE);
        assertEquals("EMPATE", PerfilConstantes.PREF_EMPATE);
    }

    @Test
    void testDescricoes() {
        assertEquals("Busca segurança e liquidez.", PerfilConstantes.DESC_CONSERVADOR);
        assertEquals("Equilíbrio entre liquidez e rentabilidade.", PerfilConstantes.DESC_MODERADO);
        assertEquals("Busca alta rentabilidade, aceitando maior risco.", PerfilConstantes.DESC_AGRESSIVO);
        assertEquals("Perfil não classificado.", PerfilConstantes.DESC_DESCONHECIDO);
    }

    @Test
    void testLiquidezStrings() {
        assertEquals("DIARIA", PerfilConstantes.LIQ_DIARIA);
        assertEquals("D+0", PerfilConstantes.LIQ_D0);
        assertEquals("MENSAL", PerfilConstantes.LIQ_MENSAL);
        assertEquals("D+30", PerfilConstantes.LIQ_D30);
    }

    @Test
    void testRentabilidade() {
        assertEquals(new BigDecimal("0.08"), PerfilConstantes.RENTABILIDADE_BAIXA);
        assertEquals(new BigDecimal("0.12"), PerfilConstantes.RENTABILIDADE_MEDIA);
    }

    @Test
    void testPrazos() {
        assertEquals(3, PerfilConstantes.PRAZO_CURTO);
        assertEquals(12, PerfilConstantes.PRAZO_MEDIO);
    }

    @Test
    void testScoreBasico() {
        assertEquals(BigDecimal.ONE, PerfilConstantes.SCORE_BAIXO);
        assertEquals(new BigDecimal("2"), PerfilConstantes.SCORE_MEDIO);
        assertEquals(new BigDecimal("3"), PerfilConstantes.SCORE_ALTO);
    }

    @Test
    void testPesosConservador() {
        assertEquals(new BigDecimal("0.6"), PerfilConstantes.PESO_LIQ_CONSERVADOR);
        assertEquals(new BigDecimal("0.2"), PerfilConstantes.PESO_RENT_CONSERVADOR);
        assertEquals(new BigDecimal("0.2"), PerfilConstantes.PESO_RISCO_CONSERVADOR);
    }

    @Test
    void testPesosModerado() {
        assertEquals(new BigDecimal("0.4"), PerfilConstantes.PESO_LIQ_MODERADO);
        assertEquals(new BigDecimal("0.4"), PerfilConstantes.PESO_RENT_MODERADO);
        assertEquals(new BigDecimal("0.2"), PerfilConstantes.PESO_RISCO_MODERADO);
    }

    @Test
    void testPesosAgressivo() {
        assertEquals(new BigDecimal("0.2"), PerfilConstantes.PESO_LIQ_AGRESSIVO);
        assertEquals(new BigDecimal("0.6"), PerfilConstantes.PESO_RENT_AGRESSIVO);
        assertEquals(new BigDecimal("0.2"), PerfilConstantes.PESO_RISCO_AGRESSIVO);
    }

    @Test
    void testPerfilProduto() {
        assertEquals("BAIXO", PerfilConstantes.PERFIL_CONSERVADOR_PRODUTO);
        assertEquals("MEDIO", PerfilConstantes.PERFIL_MODERADO_PRODUTO);
        assertEquals("ALTO", PerfilConstantes.PERFIL_AGRESSIVO_PRODUTO);
    }
}

