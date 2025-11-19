package br.com.seuempresa.invest.domain.model;

import br.com.caixaverso.invest.domain.model.Cliente;
import br.com.caixaverso.invest.domain.model.ProdutoInvestimento;
import br.com.caixaverso.invest.domain.model.SimulacaoInvestimento;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SimulacaoInvestimentoTest {

    @Test
    void testGettersAndSetters() {
        SimulacaoInvestimento s = new SimulacaoInvestimento();

        Cliente cliente = new Cliente();
        ProdutoInvestimento produto = new ProdutoInvestimento();

        LocalDateTime dataSimulacao = LocalDateTime.of(2024, 1, 10, 10, 0);
        LocalDateTime dataCriacao = LocalDateTime.of(2024, 1, 10, 14, 0);

        s.setId(1L);
        s.setCliente(cliente);
        s.setProduto(produto);
        s.setValorAplicado(new BigDecimal("2000.00"));
        s.setPrazoMeses(6);
        s.setValorFinal(new BigDecimal("2300.00"));
        s.setDataSimulacao(dataSimulacao);
        s.setDataSimulacaoDia(LocalDate.of(2024, 1, 10));
        s.setPerfilRiscoCalculado("MODERADO");
        s.setScoreRisco(new BigDecimal("0.1234"));
        s.setDataCriacao(dataCriacao);

        assertEquals(1L, s.getId());
        assertEquals(cliente, s.getCliente());
        assertEquals(produto, s.getProduto());
        assertEquals(new BigDecimal("2000.00"), s.getValorAplicado());
        assertEquals(6, s.getPrazoMeses());
        assertEquals(new BigDecimal("2300.00"), s.getValorFinal());
        assertEquals(dataSimulacao, s.getDataSimulacao());
        assertEquals(LocalDate.of(2024, 1, 10), s.getDataSimulacaoDia());
        assertEquals("MODERADO", s.getPerfilRiscoCalculado());
        assertEquals(new BigDecimal("0.1234"), s.getScoreRisco());
        assertEquals(dataCriacao, s.getDataCriacao());
    }

    @Test
    void testAllArgsConstructor() {
        Cliente c = new Cliente();
        ProdutoInvestimento p = new ProdutoInvestimento();

        LocalDateTime simulacao = LocalDateTime.now();
        LocalDateTime criacao = LocalDateTime.now();

        SimulacaoInvestimento s = new SimulacaoInvestimento(
                1L,
                c,
                p,
                new BigDecimal("5000.00"),
                12,
                new BigDecimal("5800.00"),
                simulacao,
                LocalDate.of(2024, 1, 10),
                "AGRESSIVO",
                new BigDecimal("0.5000"),
                criacao
        );

        assertEquals(1L, s.getId());
        assertEquals(c, s.getCliente());
        assertEquals(p, s.getProduto());
        assertEquals(new BigDecimal("5000.00"), s.getValorAplicado());
        assertEquals(12, s.getPrazoMeses());
        assertEquals(new BigDecimal("5800.00"), s.getValorFinal());
        assertEquals(simulacao, s.getDataSimulacao());
        assertEquals(LocalDate.of(2024, 1, 10), s.getDataSimulacaoDia());
        assertEquals("AGRESSIVO", s.getPerfilRiscoCalculado());
        assertEquals(new BigDecimal("0.5000"), s.getScoreRisco());
        assertEquals(criacao, s.getDataCriacao());
    }

    @Test
    void testBuilder() {
        Cliente c = new Cliente();
        ProdutoInvestimento p = new ProdutoInvestimento();

        SimulacaoInvestimento s = SimulacaoInvestimento.builder()
                .id(10L)
                .cliente(c)
                .produto(p)
                .valorAplicado(new BigDecimal("1500.00"))
                .prazoMeses(3)
                .valorFinal(new BigDecimal("1600.00"))
                .dataSimulacao(LocalDateTime.of(2024, 1, 1, 8, 0))
                .dataSimulacaoDia(LocalDate.of(2024, 1, 1))
                .perfilRiscoCalculado("CONSERVADOR")
                .scoreRisco(new BigDecimal("0.0500"))
                .dataCriacao(LocalDateTime.of(2024, 1, 2, 10, 0))
                .build();

        assertEquals(10L, s.getId());
        assertEquals(c, s.getCliente());
        assertEquals(p, s.getProduto());
    }

    @Test
    void testManyToOneAnnotations() throws Exception {
        Field cliente = SimulacaoInvestimento.class.getDeclaredField("cliente");
        Field produto = SimulacaoInvestimento.class.getDeclaredField("produto");

        ManyToOne manyToOneCliente = cliente.getAnnotation(ManyToOne.class);
        ManyToOne manyToOneProduto = produto.getAnnotation(ManyToOne.class);

        assertNotNull(manyToOneCliente);
        assertNotNull(manyToOneProduto);

        JoinColumn c1 = cliente.getAnnotation(JoinColumn.class);
        JoinColumn c2 = produto.getAnnotation(JoinColumn.class);

        assertNotNull(c1);
        assertEquals("cliente_id", c1.name());
        assertFalse(c1.nullable());

        assertNotNull(c2);
        assertEquals("produto_id", c2.name());
        assertFalse(c2.nullable());
    }

    @Test
    void testColumnAnnotations() throws Exception {
        Field valorAplicado = SimulacaoInvestimento.class.getDeclaredField("valorAplicado");
        Field prazoMeses = SimulacaoInvestimento.class.getDeclaredField("prazoMeses");
        Field valorFinal = SimulacaoInvestimento.class.getDeclaredField("valorFinal");
        Field dataSim = SimulacaoInvestimento.class.getDeclaredField("dataSimulacao");
        Field dataDia = SimulacaoInvestimento.class.getDeclaredField("dataSimulacaoDia");

        Column colValor = valorAplicado.getAnnotation(Column.class);
        assertNotNull(colValor);
        assertEquals(18, colValor.precision());
        assertEquals(2, colValor.scale());
        assertFalse(colValor.nullable());

        Column colPrazo = prazoMeses.getAnnotation(Column.class);
        assertNotNull(colPrazo);
        assertFalse(colPrazo.nullable());

        Column colValorFinal = valorFinal.getAnnotation(Column.class);
        assertNotNull(colValorFinal);
        assertEquals(18, colValorFinal.precision());
        assertEquals(2, colValorFinal.scale());
        assertFalse(colValorFinal.nullable());

        Column colDataSim = dataSim.getAnnotation(Column.class);
        assertNotNull(colDataSim);
        assertFalse(colDataSim.nullable());

        Column colDia = dataDia.getAnnotation(Column.class);
        assertNotNull(colDia);
        assertFalse(colDia.insertable());
        assertFalse(colDia.updatable());
    }

    @Test
    void testCreationTimestampAnnotation() throws Exception {
        Field dtCriacao = SimulacaoInvestimento.class.getDeclaredField("dataCriacao");
        assertNotNull(dtCriacao.getAnnotation(CreationTimestamp.class));
    }

    @Test
    void testEqualsAndHashCode() {
        SimulacaoInvestimento s1 = new SimulacaoInvestimento();
        s1.setId(1L);

        SimulacaoInvestimento s2 = new SimulacaoInvestimento();
        s2.setId(1L);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        SimulacaoInvestimento s3 = new SimulacaoInvestimento();
        s3.setId(2L);

        assertNotEquals(s1, s3);
    }

    @Test
    void testToString() {
        SimulacaoInvestimento s = new SimulacaoInvestimento();
        s.setId(77L);
        s.setValorAplicado(new BigDecimal("3000.00"));
        s.setPrazoMeses(9);

        String txt = s.toString();

        assertTrue(txt.contains("77"));
        assertTrue(txt.contains("3000.00"));
        assertTrue(txt.contains("9"));
    }
}