package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.ProdutoRiscoRegra;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoRiscoRegraTest {

    @Test
    void testNoArgsConstructor() {
        ProdutoRiscoRegra regra = new ProdutoRiscoRegra();

        assertNull(regra.getId());
        assertNull(regra.getFaixaMin());
        assertNull(regra.getFaixaMax());
        assertNull(regra.getRisco());
    }

    @Test
    void testAllArgsConstructor() {
        ProdutoRiscoRegra regra = new ProdutoRiscoRegra(
                1L,
                new BigDecimal("0.0500"),
                new BigDecimal("0.1000"),
                "MEDIO"
        );

        assertEquals(1L, regra.getId());
        assertEquals(new BigDecimal("0.0500"), regra.getFaixaMin());
        assertEquals(new BigDecimal("0.1000"), regra.getFaixaMax());
        assertEquals("MEDIO", regra.getRisco());
    }

    @Test
    void testBuilder() {
        ProdutoRiscoRegra regra = ProdutoRiscoRegra.builder()
                .id(2L)
                .faixaMin(new BigDecimal("0.1001"))
                .faixaMax(null) // Testando com nulo
                .risco("ALTO")
                .build();

        assertEquals(2L, regra.getId());
        assertEquals(new BigDecimal("0.1001"), regra.getFaixaMin());
        assertNull(regra.getFaixaMax());
        assertEquals("ALTO", regra.getRisco());
    }

    @Test
    void testEqualsAndHashCode() {
        // Cenário 1: Objetos com o mesmo ID devem ser iguais
        ProdutoRiscoRegra regra1 = new ProdutoRiscoRegra();
        regra1.setId(10L);
        regra1.setRisco("BAIXO");

        ProdutoRiscoRegra regra2 = new ProdutoRiscoRegra();
        regra2.setId(10L); // Mesmo ID
        regra2.setRisco("ALTO"); // Outros atributos diferentes

        assertEquals(regra1, regra2);
        assertEquals(regra1.hashCode(), regra2.hashCode());

        // Cenário 2: Objetos com IDs diferentes devem ser diferentes
        ProdutoRiscoRegra regra3 = new ProdutoRiscoRegra();
        regra3.setId(20L);

        assertNotEquals(regra1, regra3);
        assertNotEquals(regra1.hashCode(), regra3.hashCode());

        // Cenário 3: Teste de igualdade com nulo e outra classe
        assertNotEquals(regra1, null);
        assertNotEquals(regra1, "uma string");

        // Cenário 4: Teste de reflexividade (objeto igual a si mesmo)
        assertEquals(regra1, regra1);
    }

    @Test
    void testEqualsAndHashCodeWithNullIds() {
        ProdutoRiscoRegra regra1 = new ProdutoRiscoRegra();
        regra1.setId(null); // ID explícitamente nulo

        ProdutoRiscoRegra regra2 = new ProdutoRiscoRegra();
        regra2.setId(null); // ID explícitamente nulo

        assertEquals(regra1, regra2);
        assertEquals(regra1.hashCode(), regra2.hashCode());
    }

    @Test
    void testToString() {
        ProdutoRiscoRegra regra = new ProdutoRiscoRegra();
        regra.setId(99L);
        regra.setFaixaMin(BigDecimal.ZERO);
        regra.setFaixaMax(new BigDecimal("0.05"));
        regra.setRisco("BAIXO");

        String result = regra.toString();

        assertTrue(result.contains("ProdutoRiscoRegra{"));
        assertTrue(result.contains("id=99"));
        assertTrue(result.contains("faixaMin=0"));
        assertTrue(result.contains("faixaMax=0.05"));
        assertTrue(result.contains("risco='BAIXO'"));
        assertTrue(result.endsWith("}"));
    }
}