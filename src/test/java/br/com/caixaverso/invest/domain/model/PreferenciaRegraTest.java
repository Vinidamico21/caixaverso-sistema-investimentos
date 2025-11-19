package br.com.caixaverso.invest.domain.model;

import br.com.caixaverso.invest.infra.persistence.entity.PreferenciaRegra;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class PreferenciaRegraTest {

    @Test
    void testGettersAndSetters() {
        PreferenciaRegra regra = new PreferenciaRegra();

        regra.setId(1L);
        regra.setTipoPreferencia("LIQUIDEZ");
        regra.setPontuacao(8);

        assertEquals(1L, regra.getId());
        assertEquals("LIQUIDEZ", regra.getTipoPreferencia());
        assertEquals(8, regra.getPontuacao());
    }

    @Test
    void testDefaultConstructor() {
        PreferenciaRegra regra = new PreferenciaRegra();
        assertNotNull(regra);
    }

    @Test
    void testAnnotations() throws Exception {
        Field id = PreferenciaRegra.class.getDeclaredField("id");
        Field tipoPreferencia = PreferenciaRegra.class.getDeclaredField("tipoPreferencia");

        // @Id
        assertNotNull(id.getAnnotation(Id.class), "Campo id deve ter @Id");

        // @GeneratedValue(strategy = IDENTITY)
        GeneratedValue gen = id.getAnnotation(GeneratedValue.class);
        assertNotNull(gen, "Campo id deve ter @GeneratedValue");
        assertEquals(GenerationType.IDENTITY, gen.strategy());

        // @Column(name = "tipo_preferencia")
        Column colTipo = tipoPreferencia.getAnnotation(Column.class);
        assertNotNull(colTipo, "tipoPreferencia deve ter @Column");
        assertEquals("tipo_preferencia", colTipo.name());
    }

    @Test
    void testEqualsAndHashCode() {
        PreferenciaRegra r1 = new PreferenciaRegra();
        r1.setId(10L);

        PreferenciaRegra r2 = new PreferenciaRegra();
        r2.setId(10L);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        PreferenciaRegra r3 = new PreferenciaRegra();
        r3.setId(20L);

        assertNotEquals(r1, r3);
    }

    @Test
    void testToString() {
        PreferenciaRegra regra = new PreferenciaRegra();
        regra.setId(1L);
        regra.setTipoPreferencia("RENTABILIDADE");
        regra.setPontuacao(12);

        String txt = regra.toString();

        assertTrue(txt.contains("1"));
        assertTrue(txt.contains("RENTABILIDADE"));
        assertTrue(txt.contains("12"));
    }
}