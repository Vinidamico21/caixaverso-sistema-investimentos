package br.com.caixaverso.invest.domain.model;

import br.com.caixaverso.invest.infra.persistence.entity.Cliente;
import br.com.caixaverso.invest.infra.persistence.entity.Investimento;
import br.com.caixaverso.invest.infra.persistence.entity.ProdutoInvestimento;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InvestimentoTest {

    @Test
    void testGettersAndSetters() {
        Investimento investimento = new Investimento();

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        ProdutoInvestimento produto = new ProdutoInvestimento();
        produto.setId(2L);

        LocalDateTime aporte = LocalDateTime.of(2024, 1, 10, 12, 0);
        LocalDateTime criacao = LocalDateTime.of(2024, 1, 11, 9, 0);

        investimento.setId(10L);
        investimento.setCliente(cliente);
        investimento.setProduto(produto);
        investimento.setValorAplicado(new BigDecimal("1500.00"));
        investimento.setPrazoMeses(12);
        investimento.setDataAporte(aporte);
        investimento.setStatus("ATIVO");
        investimento.setDataCriacao(criacao);

        assertEquals(10L, investimento.getId());
        assertEquals(cliente, investimento.getCliente());
        assertEquals(produto, investimento.getProduto());
        assertEquals(new BigDecimal("1500.00"), investimento.getValorAplicado());
        assertEquals(12, investimento.getPrazoMeses());
        assertEquals(aporte, investimento.getDataAporte());
        assertEquals("ATIVO", investimento.getStatus());
        assertEquals(criacao, investimento.getDataCriacao());
    }

    @Test
    void testAllArgsConstructor() {
        Cliente cliente = new Cliente();
        ProdutoInvestimento produto = new ProdutoInvestimento();

        LocalDateTime aporte = LocalDateTime.now();
        LocalDateTime criacao = LocalDateTime.now();

        Investimento investimento = new Investimento(
                1L,
                cliente,
                produto,
                new BigDecimal("2500.00"),
                6,
                aporte,
                "CONCLUIDO",
                criacao
        );

        assertEquals(1L, investimento.getId());
        assertEquals(cliente, investimento.getCliente());
        assertEquals(produto, investimento.getProduto());
        assertEquals(new BigDecimal("2500.00"), investimento.getValorAplicado());
        assertEquals(6, investimento.getPrazoMeses());
        assertEquals(aporte, investimento.getDataAporte());
        assertEquals("CONCLUIDO", investimento.getStatus());
        assertEquals(criacao, investimento.getDataCriacao());
    }

    @Test
    void testBuilder() {
        Cliente cliente = new Cliente();
        ProdutoInvestimento produto = new ProdutoInvestimento();

        Investimento inv = Investimento.builder()
                .id(5L)
                .cliente(cliente)
                .produto(produto)
                .valorAplicado(new BigDecimal("999.99"))
                .prazoMeses(3)
                .dataAporte(LocalDateTime.of(2024, 1, 1, 8, 0))
                .status("ATIVO")
                .dataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0))
                .build();

        assertEquals(5L, inv.getId());
        assertEquals(cliente, inv.getCliente());
        assertEquals(produto, inv.getProduto());
        assertEquals(new BigDecimal("999.99"), inv.getValorAplicado());
        assertEquals(3, inv.getPrazoMeses());
        assertEquals(LocalDateTime.of(2024, 1, 1, 8, 0), inv.getDataAporte());
        assertEquals("ATIVO", inv.getStatus());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), inv.getDataCriacao());
    }

    @Test
    void testManyToOneAnnotations() throws Exception {
        Field cliente = Investimento.class.getDeclaredField("cliente");
        Field produto = Investimento.class.getDeclaredField("produto");

        ManyToOne mt1 = cliente.getAnnotation(ManyToOne.class);
        ManyToOne mt2 = produto.getAnnotation(ManyToOne.class);

        assertNotNull(mt1);
        assertNotNull(mt2);

        JoinColumn jc1 = cliente.getAnnotation(JoinColumn.class);
        JoinColumn jc2 = produto.getAnnotation(JoinColumn.class);

        assertNotNull(jc1);
        assertEquals("cliente_id", jc1.name());
        assertFalse(jc1.nullable());

        assertNotNull(jc2);
        assertEquals("produto_id", jc2.name());
        assertFalse(jc2.nullable());
    }

    @Test
    void testColumnAnnotations() throws Exception {
        Field valor = Investimento.class.getDeclaredField("valorAplicado");
        Field dataAporte = Investimento.class.getDeclaredField("dataAporte");
        Field status = Investimento.class.getDeclaredField("status");

        Column colValor = valor.getAnnotation(Column.class);
        assertNotNull(colValor);
        assertEquals(18, colValor.precision());
        assertEquals(2, colValor.scale());
        assertFalse(colValor.nullable());

        Column colAporte = dataAporte.getAnnotation(Column.class);
        assertNotNull(colAporte);
        assertFalse(colAporte.nullable());

        Column colStatus = status.getAnnotation(Column.class);
        assertNotNull(colStatus);
        assertEquals(20, colStatus.length());
        assertFalse(colStatus.nullable());
    }

    @Test
    void testCreationTimestampAnnotation() throws Exception {
        Field field = Investimento.class.getDeclaredField("dataCriacao");
        assertNotNull(field.getAnnotation(CreationTimestamp.class));
    }

    @Test
    void testEqualsAndHashCode() {
        Investimento i1 = new Investimento();
        i1.setId(10L);

        Investimento i2 = new Investimento();
        i2.setId(10L);

        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());

        Investimento i3 = new Investimento();
        i3.setId(20L);

        assertNotEquals(i1, i3);
    }

    @Test
    void testToString() {
        Investimento inv = new Investimento();
        inv.setId(1L);
        inv.setStatus("ATIVO");
        inv.setPrazoMeses(6);

        String txt = inv.toString();

        assertTrue(txt.contains("1"));
        assertTrue(txt.contains("ATIVO"));
        assertTrue(txt.contains("6"));
    }
}
