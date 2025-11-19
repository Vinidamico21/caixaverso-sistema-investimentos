package br.com.caixaverso.invest.domain.model;

import br.com.caixaverso.invest.infra.persistence.entity.ProdutoRiscoRegra;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoRiscoRegraTest {

    @Test
    void testGettersAndSetters() {
        ProdutoRiscoRegra regra = new ProdutoRiscoRegra();

        regra.setId(1L);
        regra.setFaixaMin(new BigDecimal("0.0100"));
        regra.setFaixaMax(new BigDecimal("0.0500"));
        regra.setRisco("BAIXO");

        assertEquals(1L, regra.getId());
        assertEquals(new BigDecimal("0.0100"), regra.getFaixaMin());
        assertEquals(new BigDecimal("0.0500"), regra.getFaixaMax());
        assertEquals("BAIXO", regra.getRisco());
    }

    @Test
    void testDefaultConstructor() {
        ProdutoRiscoRegra regra = new ProdutoRiscoRegra();
        assertNotNull(regra);
    }

    @Test
    void testAllArgsConstructor() {
        ProdutoRiscoRegra regra = new ProdutoRiscoRegra(
                1L,
                new BigDecimal("0.0100"),
                new BigDecimal("0.0500"),
                "BAIXO"
        );

        assertEquals(1L, regra.getId());
        assertEquals(new BigDecimal("0.0100"), regra.getFaixaMin());
        assertEquals(new BigDecimal("0.0500"), regra.getFaixaMax());
        assertEquals("BAIXO", regra.getRisco());
    }

    @Test
    void testBuilder() {
        ProdutoRiscoRegra regra = ProdutoRiscoRegra.builder()
                .id(1L)
                .faixaMin(new BigDecimal("0.0100"))
                .faixaMax(new BigDecimal("0.0500"))
                .risco("BAIXO")
                .build();

        assertEquals(1L, regra.getId());
        assertEquals(new BigDecimal("0.0100"), regra.getFaixaMin());
        assertEquals(new BigDecimal("0.0500"), regra.getFaixaMax());
        assertEquals("BAIXO", regra.getRisco());
    }

    @Test
    void testAnnotations() throws Exception {
        Field id = ProdutoRiscoRegra.class.getDeclaredField("id");
        Field faixaMin = ProdutoRiscoRegra.class.getDeclaredField("faixaMin");
        Field faixaMax = ProdutoRiscoRegra.class.getDeclaredField("faixaMax");
        Field risco = ProdutoRiscoRegra.class.getDeclaredField("risco");

        // --- id ---
        assertNotNull(id.getAnnotation(jakarta.persistence.Id.class));
        jakarta.persistence.GeneratedValue gen = id.getAnnotation(jakarta.persistence.GeneratedValue.class);
        assertNotNull(gen);
        assertEquals(jakarta.persistence.GenerationType.IDENTITY, gen.strategy());

        // --- faixaMin ---
        jakarta.persistence.Column colMin = faixaMin.getAnnotation(jakarta.persistence.Column.class);
        assertNotNull(colMin);
        assertFalse(colMin.nullable());
        assertEquals(10, colMin.precision());
        assertEquals(4, colMin.scale());
        assertEquals("rentabilidade_min", colMin.name());

        // --- faixaMax ---
        jakarta.persistence.Column colMax = faixaMax.getAnnotation(jakarta.persistence.Column.class);
        assertNotNull(colMax);
        assertFalse(colMax.nullable());
        assertEquals(10, colMax.precision());
        assertEquals(4, colMax.scale());
        assertEquals("rentabilidade_max", colMax.name());

        // --- risco ---
        jakarta.persistence.Column colRisco = risco.getAnnotation(jakarta.persistence.Column.class);
        assertNotNull(colRisco);
        assertFalse(colRisco.nullable());
        assertEquals(20, colRisco.length());
        assertEquals("risco", colRisco.name());
    }

    @Test
    void testEqualsAndHashCode() {
        ProdutoRiscoRegra r1 = new ProdutoRiscoRegra();
        r1.setId(5L);

        ProdutoRiscoRegra r2 = new ProdutoRiscoRegra();
        r2.setId(5L);

        ProdutoRiscoRegra r3 = new ProdutoRiscoRegra();
        r3.setId(10L);

        // Teste igualdade
        assertEquals(r1, r2);
        assertEquals(r1, r1); // mesma instância
        assertNotEquals(r1, r3);
        assertNotEquals(r1, null);
        assertNotEquals(r1, new Object());

        // Teste hashCode
        assertEquals(r1.hashCode(), r2.hashCode());
        assertNotEquals(r1.hashCode(), r3.hashCode());
    }

    @Test
    void testToString() {
        ProdutoRiscoRegra regra = new ProdutoRiscoRegra();
        regra.setId(1L);
        regra.setFaixaMin(new BigDecimal("0.0200"));
        regra.setFaixaMax(new BigDecimal("0.0600"));
        regra.setRisco("MEDIO");

        String result = regra.toString();

        assertTrue(result.contains("ProdutoRiscoRegra{"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("faixaMin=0.0200"));
        assertTrue(result.contains("faixaMax=0.0600"));
        assertTrue(result.contains("risco='MEDIO'"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testEqualsWithNullId() {
        ProdutoRiscoRegra r1 = new ProdutoRiscoRegra();
        ProdutoRiscoRegra r2 = new ProdutoRiscoRegra();

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.setId(null);
        r2.setId(null);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testTableAnnotation() {
        jakarta.persistence.Table table = ProdutoRiscoRegra.class.getAnnotation(jakarta.persistence.Table.class);
        assertNotNull(table);
        assertEquals("ProdutoRiscoRegra", table.name());
    }

    @Test
    void testBigDecimalPrecisionAndScale() {
        ProdutoRiscoRegra regra = new ProdutoRiscoRegra();
        regra.setFaixaMin(new BigDecimal("0.1234"));
        regra.setFaixaMax(new BigDecimal("12.3456"));

        assertEquals(new BigDecimal("0.1234"), regra.getFaixaMin());
        assertEquals(new BigDecimal("12.3456"), regra.getFaixaMax());
    }
}