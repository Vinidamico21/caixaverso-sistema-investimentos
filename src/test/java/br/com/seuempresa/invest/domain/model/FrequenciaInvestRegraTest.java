package br.com.seuempresa.invest.domain.model;

import br.com.caixaverso.invest.domain.model.FrequenciaInvestRegra;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class FrequenciaInvestRegraTest {

    @Test
    void testGettersAndSetters() {
        FrequenciaInvestRegra regra = new FrequenciaInvestRegra();

        regra.setId(1L);
        regra.setQuantidadeMin(2);
        regra.setQuantidadeMax(5);
        regra.setPontuacao(10);

        assertEquals(1L, regra.getId());
        assertEquals(2, regra.getQuantidadeMin());
        assertEquals(5, regra.getQuantidadeMax());
        assertEquals(10, regra.getPontuacao());
    }

    @Test
    void testDefaultConstructor() {
        FrequenciaInvestRegra regra = new FrequenciaInvestRegra();
        assertNotNull(regra);
    }

    @Test
    void testAnnotationsOnFields() throws Exception {
        Field id = FrequenciaInvestRegra.class.getDeclaredField("id");
        Field qMin = FrequenciaInvestRegra.class.getDeclaredField("quantidadeMin");
        Field qMax = FrequenciaInvestRegra.class.getDeclaredField("quantidadeMax");

        // @Id
        assertNotNull(id.getAnnotation(Id.class), "Campo id deve ter @Id");

        // @GeneratedValue
        GeneratedValue generated = id.getAnnotation(GeneratedValue.class);
        assertNotNull(generated, "Campo id deve ter @GeneratedValue");
        assertEquals(GenerationType.IDENTITY, generated.strategy());

        // @Column(name = "quantidade_min")
        Column colMin = qMin.getAnnotation(Column.class);
        assertNotNull(colMin);
        assertEquals("quantidade_min", colMin.name());

        // @Column(name = "quantidade_max")
        Column colMax = qMax.getAnnotation(Column.class);
        assertNotNull(colMax);
        assertEquals("quantidade_max", colMax.name());
    }

    @Test
    void testToString() {
        FrequenciaInvestRegra regra = new FrequenciaInvestRegra();
        regra.setId(1L);
        regra.setQuantidadeMin(3);
        regra.setQuantidadeMax(7);
        regra.setPontuacao(15);

        String txt = regra.toString();

        assertTrue(txt.contains("1"));
        assertTrue(txt.contains("3"));
        assertTrue(txt.contains("7"));
        assertTrue(txt.contains("15"));
    }

    @Test
    void testEqualsAndHashCode() {
        FrequenciaInvestRegra r1 = new FrequenciaInvestRegra();
        r1.setId(10L);

        FrequenciaInvestRegra r2 = new FrequenciaInvestRegra();
        r2.setId(10L);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        FrequenciaInvestRegra r3 = new FrequenciaInvestRegra();
        r3.setId(20L);

        assertNotEquals(r1, r3);
    }
}