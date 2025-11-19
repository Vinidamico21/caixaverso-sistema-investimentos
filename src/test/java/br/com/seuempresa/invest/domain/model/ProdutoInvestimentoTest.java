package br.com.seuempresa.invest.domain.model;

import br.com.caixaverso.invest.domain.model.ProdutoInvestimento;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoInvestimentoTest {

    @Test
    void testGettersAndSetters() {
        ProdutoInvestimento p = new ProdutoInvestimento();

        p.setId(1L);
        p.setCodigo("LCA123");
        p.setNome("Letra de Crédito do Agronegócio");
        p.setTipo("RENDA_FIXA");
        p.setRisco("BAIXO");
        p.setTaxaAnual(new BigDecimal("0.1234"));
        p.setLiquidez("DIARIA");
        p.setPrazoMinMeses(3);
        p.setPrazoMaxMeses(24);
        p.setValorMinimo(new BigDecimal("1000.00"));
        p.setValorMaximo(new BigDecimal("50000.00"));
        p.setAtivo(true);
        p.setDataCriacao(LocalDateTime.of(2024, 1, 1, 12, 0));

        assertEquals(1L, p.getId());
        assertEquals("LCA123", p.getCodigo());
        assertEquals("Letra de Crédito do Agronegócio", p.getNome());
        assertEquals("RENDA_FIXA", p.getTipo());
        assertEquals("BAIXO", p.getRisco());
        assertEquals(new BigDecimal("0.1234"), p.getTaxaAnual());
        assertEquals("DIARIA", p.getLiquidez());
        assertEquals(3, p.getPrazoMinMeses());
        assertEquals(24, p.getPrazoMaxMeses());
        assertEquals(new BigDecimal("1000.00"), p.getValorMinimo());
        assertEquals(new BigDecimal("50000.00"), p.getValorMaximo());
        assertTrue(p.getAtivo());
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 0), p.getDataCriacao());
    }

    @Test
    void testAllArgsConstructor() {
        ProdutoInvestimento p = new ProdutoInvestimento(
                1L,
                "CDB987",
                "CDB Premium",
                "RENDA_FIXA",
                "MEDIO",
                new BigDecimal("0.0950"),
                "D+30",
                6,
                36,
                new BigDecimal("500.00"),
                new BigDecimal("10000.00"),
                false,
                LocalDateTime.now()
        );

        assertEquals(1L, p.getId());
        assertEquals("CDB987", p.getCodigo());
        assertEquals("CDB Premium", p.getNome());
        assertEquals("RENDA_FIXA", p.getTipo());
        assertEquals("MEDIO", p.getRisco());
        assertEquals(new BigDecimal("0.0950"), p.getTaxaAnual());
        assertEquals("D+30", p.getLiquidez());
        assertEquals(6, p.getPrazoMinMeses());
        assertEquals(36, p.getPrazoMaxMeses());
        assertEquals(new BigDecimal("500.00"), p.getValorMinimo());
        assertEquals(new BigDecimal("10000.00"), p.getValorMaximo());
        assertFalse(p.getAtivo());
    }

    @Test
    void testBuilder() {
        ProdutoInvestimento p = ProdutoInvestimento.builder()
                .id(5L)
                .codigo("TESOURO")
                .nome("Tesouro Selic 2027")
                .tipo("TESOURO_DIRETO")
                .risco("BAIXO")
                .taxaAnual(new BigDecimal("0.0150"))
                .liquidez("D+1")
                .prazoMinMeses(1)
                .prazoMaxMeses(36)
                .valorMinimo(new BigDecimal("30.00"))
                .valorMaximo(new BigDecimal("100000.00"))
                .ativo(true)
                .dataCriacao(LocalDateTime.of(2024, 5, 1, 8, 0))
                .build();

        assertEquals(5L, p.getId());
        assertEquals("TESOURO", p.getCodigo());
        assertEquals("Tesouro Selic 2027", p.getNome());
    }

    @Test
    void testAnnotations() throws Exception {
        Field id = ProdutoInvestimento.class.getDeclaredField("id");
        Field codigo = ProdutoInvestimento.class.getDeclaredField("codigo");
        Field nome = ProdutoInvestimento.class.getDeclaredField("nome");
        Field tipo = ProdutoInvestimento.class.getDeclaredField("tipo");
        Field risco = ProdutoInvestimento.class.getDeclaredField("risco");
        Field taxa = ProdutoInvestimento.class.getDeclaredField("taxaAnual");
        Field liquidez = ProdutoInvestimento.class.getDeclaredField("liquidez");
        Field valorMin = ProdutoInvestimento.class.getDeclaredField("valorMinimo");

        // id
        assertNotNull(id.getAnnotation(Id.class));
        GeneratedValue gen = id.getAnnotation(GeneratedValue.class);
        assertEquals(GenerationType.IDENTITY, gen.strategy());

        // codigo
        Column colCodigo = codigo.getAnnotation(Column.class);
        assertNotNull(colCodigo);
        assertFalse(colCodigo.nullable());
        assertTrue(colCodigo.unique());
        assertEquals(50, colCodigo.length());

        // nome
        Column colNome = nome.getAnnotation(Column.class);
        assertNotNull(colNome);
        assertFalse(colNome.nullable());
        assertEquals(150, colNome.length());

        // tipo
        Column colTipo = tipo.getAnnotation(Column.class);
        assertNotNull(colTipo);
        assertFalse(colTipo.nullable());
        assertEquals(30, colTipo.length());

        // risco
        Column colRisco = risco.getAnnotation(Column.class);
        assertNotNull(colRisco);
        assertFalse(colRisco.nullable());
        assertEquals(20, colRisco.length());

        // taxa anual
        Column colTaxa = taxa.getAnnotation(Column.class);
        assertNotNull(colTaxa);
        assertFalse(colTaxa.nullable());
        assertEquals(7, colTaxa.precision());
        assertEquals(4, colTaxa.scale());

        // liquidez
        Column colLiq = liquidez.getAnnotation(Column.class);
        assertNotNull(colLiq);
        assertFalse(colLiq.nullable());
        assertEquals(30, colLiq.length());

        // valor mínimo
        Column colValorMin = valorMin.getAnnotation(Column.class);
        assertNotNull(colValorMin);
        assertFalse(colValorMin.nullable());
        assertEquals(18, colValorMin.precision());
        assertEquals(2, colValorMin.scale());
    }

    @Test
    void testCreationTimestampAnnotation() throws Exception {
        Field field = ProdutoInvestimento.class.getDeclaredField("dataCriacao");
        assertNotNull(field.getAnnotation(CreationTimestamp.class));
    }

    @Test
    void testEqualsAndHashCode() {
        ProdutoInvestimento p1 = new ProdutoInvestimento();
        p1.setId(1L);

        ProdutoInvestimento p2 = new ProdutoInvestimento();
        p2.setId(1L);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        ProdutoInvestimento p3 = new ProdutoInvestimento();
        p3.setId(2L);

        assertNotEquals(p1, p3);
    }

    @Test
    void testToString() {
        ProdutoInvestimento p = new ProdutoInvestimento();
        p.setId(99L);
        p.setCodigo("XPTO");
        p.setNome("Produto XPTO");

        String txt = p.toString();

        assertTrue(txt.contains("99"));
        assertTrue(txt.contains("XPTO"));
        assertTrue(txt.contains("Produto XPTO"));
    }
}