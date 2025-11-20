package br.com.caixaverso.invest.domain.model;

import br.com.caixaverso.invest.infra.persistence.entity.ProdutoInvestimento;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SimulacaoResultadoTest {

    @Test
    void testConstructorAndGetters() {
        ProdutoInvestimento produto = new ProdutoInvestimento();
        produto.setId(10L);
        produto.setNome("CDB Teste");

        BigDecimal valorFinal = new BigDecimal("2500.50");
        int prazo = 12;

        SimulacaoResultado resultado = new SimulacaoResultado(produto, valorFinal, prazo);

        assertEquals(produto, resultado.produto());
        assertEquals(new BigDecimal("2500.50"), resultado.valorFinal());
        assertEquals(12, resultado.prazoMeses());
    }

    @Test
    void testFieldsAreFinalAndImmutable() throws Exception {
        Field produtoField = SimulacaoResultado.class.getDeclaredField("produto");
        Field valorFinalField = SimulacaoResultado.class.getDeclaredField("valorFinal");
        Field prazoMesesField = SimulacaoResultado.class.getDeclaredField("prazoMeses");

        // Teste se os campos são final
        assertTrue(Modifier.isFinal(produtoField.getModifiers()),
                "Campo produto deve ser final");
        assertTrue(Modifier.isFinal(valorFinalField.getModifiers()),
                "Campo valorFinal deve ser final");
        assertTrue(Modifier.isFinal(prazoMesesField.getModifiers()),
                "Campo prazoMeses deve ser final");

        // Teste se os campos são private
        assertTrue(Modifier.isPrivate(produtoField.getModifiers()),
                "Campo produto deve ser private");
        assertTrue(Modifier.isPrivate(valorFinalField.getModifiers()),
                "Campo valorFinal deve ser private");
        assertTrue(Modifier.isPrivate(prazoMesesField.getModifiers()),
                "Campo prazoMeses deve ser private");
    }

    @Test
    void testEqualsAndHashCode() {
        ProdutoInvestimento produto1 = new ProdutoInvestimento();
        produto1.setId(1L);

        ProdutoInvestimento produto2 = new ProdutoInvestimento();
        produto2.setId(2L);

        SimulacaoResultado r1 = new SimulacaoResultado(produto1, new BigDecimal("1000.00"), 12);
        SimulacaoResultado r2 = new SimulacaoResultado(produto1, new BigDecimal("1000.00"), 12);
        SimulacaoResultado r3 = new SimulacaoResultado(produto2, new BigDecimal("2000.00"), 24);

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
        ProdutoInvestimento produto = new ProdutoInvestimento();
        produto.setId(1L);
        produto.setNome("CDB Premium");

        SimulacaoResultado resultado = new SimulacaoResultado(
                produto,
                new BigDecimal("5000.00"),
                24
        );

        String result = resultado.toString();

        assertTrue(result.contains("SimulacaoResultado"));
        assertTrue(result.contains("produto=" + produto));
        assertTrue(result.contains("valorFinal=5000.00"));
        assertTrue(result.contains("prazoMeses=24"));
    }

    @Test
    void testWithNullValues() {
        SimulacaoResultado resultado = new SimulacaoResultado(null, null, null);

        assertNull(resultado.produto());
        assertNull(resultado.valorFinal());
        assertNull(resultado.prazoMeses());
    }

    @Test
    void testEqualsWithNullValues() {
        SimulacaoResultado r1 = new SimulacaoResultado(null, null, null);
        SimulacaoResultado r2 = new SimulacaoResultado(null, null, null);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testImmutability() {
        ProdutoInvestimento produto = new ProdutoInvestimento();
        produto.setId(1L);

        BigDecimal valorFinal = new BigDecimal("1000.00");
        Integer prazo = 12;

        SimulacaoResultado resultado = new SimulacaoResultado(produto, valorFinal, prazo);

        // Os valores não devem mudar após a criação
        assertEquals(produto, resultado.produto());
        assertEquals(valorFinal, resultado.valorFinal());
        assertEquals(prazo, resultado.prazoMeses());
    }
}