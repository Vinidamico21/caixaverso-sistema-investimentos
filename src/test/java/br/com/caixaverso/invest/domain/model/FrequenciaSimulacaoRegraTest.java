package br.com.caixaverso.invest.domain.model;

import br.com.caixaverso.invest.domain.model.FrequenciaSimulacaoRegra;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class FrequenciaSimulacaoRegraTest {

    @Test
    void testGettersAndSetters() {
        FrequenciaSimulacaoRegra regra = new FrequenciaSimulacaoRegra();

        regra.setId(1L);
        regra.setQuantidadeMin(2);
        regra.setQuantidadeMax(6);
        regra.setPontuacao(12);

        assertEquals(1L, regra.getId());
        assertEquals(2, regra.getQuantidadeMin());
        assertEquals(6, regra.getQuantidadeMax());
        assertEquals(12, regra.getPontuacao());
    }

    @Test
    void testDefaultConstructor() {
        FrequenciaSimulacaoRegra regra = new FrequenciaSimulacaoRegra();
        assertNotNull(regra);
    }

    @Test
    void testAllArgsConstructor() {
        FrequenciaSimulacaoRegra regra = new FrequenciaSimulacaoRegra(1L, 2, 6, 12);

        assertEquals(1L, regra.getId());
        assertEquals(2, regra.getQuantidadeMin());
        assertEquals(6, regra.getQuantidadeMax());
        assertEquals(12, regra.getPontuacao());
    }

    @Test
    void testBuilder() {
        FrequenciaSimulacaoRegra regra = FrequenciaSimulacaoRegra.builder()
                .id(1L)
                .quantidadeMin(2)
                .quantidadeMax(6)
                .pontuacao(12)
                .build();

        assertEquals(1L, regra.getId());
        assertEquals(2, regra.getQuantidadeMin());
        assertEquals(6, regra.getQuantidadeMax());
        assertEquals(12, regra.getPontuacao());
    }

    @Test
    void testAnnotationsOnFields() throws Exception {
        Field id = FrequenciaSimulacaoRegra.class.getDeclaredField("id");
        Field qMin = FrequenciaSimulacaoRegra.class.getDeclaredField("quantidadeMin");
        Field qMax = FrequenciaSimulacaoRegra.class.getDeclaredField("quantidadeMax");
        Field pontuacao = FrequenciaSimulacaoRegra.class.getDeclaredField("pontuacao");

        // @Id e @GeneratedValue
        assertNotNull(id.getAnnotation(jakarta.persistence.Id.class));
        assertNotNull(id.getAnnotation(jakarta.persistence.GeneratedValue.class));

        // @Column para quantidade_min
        jakarta.persistence.Column colMin = qMin.getAnnotation(jakarta.persistence.Column.class);
        assertNotNull(colMin);
        assertEquals("quantidade_min", colMin.name());

        // @Column para quantidade_max
        jakarta.persistence.Column colMax = qMax.getAnnotation(jakarta.persistence.Column.class);
        assertNotNull(colMax);
        assertEquals("quantidade_max", colMax.name());

        // @Column para pontuacao
        jakarta.persistence.Column colPontuacao = pontuacao.getAnnotation(jakarta.persistence.Column.class);
        assertNotNull(colPontuacao);
        assertEquals("pontuacao", colPontuacao.name());
    }

    @Test
    void testEqualsAndHashCode() {
        FrequenciaSimulacaoRegra r1 = new FrequenciaSimulacaoRegra();
        r1.setId(10L);

        FrequenciaSimulacaoRegra r2 = new FrequenciaSimulacaoRegra();
        r2.setId(10L);

        FrequenciaSimulacaoRegra r3 = new FrequenciaSimulacaoRegra();
        r3.setId(20L);

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
        FrequenciaSimulacaoRegra regra = new FrequenciaSimulacaoRegra();
        regra.setId(1L);
        regra.setQuantidadeMin(3);
        regra.setQuantidadeMax(8);
        regra.setPontuacao(20);

        String result = regra.toString();

        assertTrue(result.contains("FrequenciaSimulacaoRegra{"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("quantidadeMin=3"));
        assertTrue(result.contains("quantidadeMax=8"));
        assertTrue(result.contains("pontuacao=20"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testEqualsWithNullId() {
        FrequenciaSimulacaoRegra r1 = new FrequenciaSimulacaoRegra();
        FrequenciaSimulacaoRegra r2 = new FrequenciaSimulacaoRegra();

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.setId(null);
        r2.setId(null);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testTableAnnotation() {
        Table table = FrequenciaSimulacaoRegra.class.getAnnotation(Table.class);
        assertNotNull(table);
        assertEquals("perfil_freq_simulacao_regra", table.name());
    }
}