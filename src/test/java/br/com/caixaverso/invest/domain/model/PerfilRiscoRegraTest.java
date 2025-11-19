package br.com.caixaverso.invest.domain.model;

import br.com.caixaverso.invest.infra.persistence.entity.PerfilRiscoRegra;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class PerfilRiscoRegraTest {

    @Test
    void testGettersAndSetters() {
        PerfilRiscoRegra regra = new PerfilRiscoRegra();

        regra.setId(1L);
        regra.setScoreMin(10);
        regra.setScoreMax(20);
        regra.setPerfil("MODERADO");

        assertEquals(1L, regra.getId());
        assertEquals(10, regra.getScoreMin());
        assertEquals(20, regra.getScoreMax());
        assertEquals("MODERADO", regra.getPerfil());
    }

    @Test
    void testDefaultConstructor() {
        PerfilRiscoRegra regra = new PerfilRiscoRegra();
        assertNotNull(regra);
    }

    @Test
    void testAllArgsConstructor() {
        PerfilRiscoRegra regra = new PerfilRiscoRegra(1L, 10, 20, "MODERADO");

        assertEquals(1L, regra.getId());
        assertEquals(10, regra.getScoreMin());
        assertEquals(20, regra.getScoreMax());
        assertEquals("MODERADO", regra.getPerfil());
    }

    @Test
    void testBuilder() {
        PerfilRiscoRegra regra = PerfilRiscoRegra.builder()
                .id(1L)
                .scoreMin(10)
                .scoreMax(20)
                .perfil("MODERADO")
                .build();

        assertEquals(1L, regra.getId());
        assertEquals(10, regra.getScoreMin());
        assertEquals(20, regra.getScoreMax());
        assertEquals("MODERADO", regra.getPerfil());
    }

    @Test
    void testAnnotations() throws Exception {
        Field id = PerfilRiscoRegra.class.getDeclaredField("id");
        Field scoreMin = PerfilRiscoRegra.class.getDeclaredField("scoreMin");
        Field scoreMax = PerfilRiscoRegra.class.getDeclaredField("scoreMax");
        Field perfil = PerfilRiscoRegra.class.getDeclaredField("perfil");

        // Campo id
        assertNotNull(id.getAnnotation(jakarta.persistence.Id.class), "id deve ter @Id");
        jakarta.persistence.GeneratedValue gen = id.getAnnotation(jakarta.persistence.GeneratedValue.class);
        assertNotNull(gen);
        assertEquals(jakarta.persistence.GenerationType.IDENTITY, gen.strategy());

        // scoreMin
        jakarta.persistence.Column colMin = scoreMin.getAnnotation(jakarta.persistence.Column.class);
        assertNotNull(colMin);
        assertEquals("score_min", colMin.name());
        assertFalse(colMin.nullable());

        // scoreMax
        jakarta.persistence.Column colMax = scoreMax.getAnnotation(jakarta.persistence.Column.class);
        assertNotNull(colMax);
        assertEquals("score_max", colMax.name());
        assertFalse(colMax.nullable());

        // perfil
        jakarta.persistence.Column colPerfil = perfil.getAnnotation(jakarta.persistence.Column.class);
        assertNotNull(colPerfil);
        assertEquals("perfil", colPerfil.name());
        assertFalse(colPerfil.nullable());
        assertEquals(50, colPerfil.length());
    }

    @Test
    void testEqualsAndHashCode() {
        PerfilRiscoRegra r1 = new PerfilRiscoRegra();
        r1.setId(100L);

        PerfilRiscoRegra r2 = new PerfilRiscoRegra();
        r2.setId(100L);

        PerfilRiscoRegra r3 = new PerfilRiscoRegra();
        r3.setId(200L);

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
        PerfilRiscoRegra regra = new PerfilRiscoRegra();
        regra.setId(1L);
        regra.setScoreMin(5);
        regra.setScoreMax(15);
        regra.setPerfil("AGRESSIVO");

        String result = regra.toString();

        assertTrue(result.contains("PerfilRiscoRegra{"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("scoreMin=5"));
        assertTrue(result.contains("scoreMax=15"));
        assertTrue(result.contains("perfil='AGRESSIVO'"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testEqualsWithNullId() {
        PerfilRiscoRegra r1 = new PerfilRiscoRegra();
        PerfilRiscoRegra r2 = new PerfilRiscoRegra();

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.setId(null);
        r2.setId(null);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testTableAnnotation() {
        jakarta.persistence.Table table = PerfilRiscoRegra.class.getAnnotation(jakarta.persistence.Table.class);
        assertNotNull(table);
        assertEquals("PerfilRiscoRegra", table.name());
    }
}