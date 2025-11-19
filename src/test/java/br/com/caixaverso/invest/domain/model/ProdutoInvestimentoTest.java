package br.com.caixaverso.invest.domain.model;

import br.com.caixaverso.invest.infra.persistence.entity.ProdutoInvestimento;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoInvestimentoTest {

    private ProdutoInvestimento criarBase(Long id, String codigo) {
        return ProdutoInvestimento.builder()
                .id(id)
                .codigo(codigo)
                .nome("Produto " + codigo)
                .tipo("RENDA_FIXA")
                .risco("BAIXO")
                .taxaAnual(new BigDecimal("0.10"))
                .liquidez("D+1")
                .prazoMinMeses(6)
                .prazoMaxMeses(24)
                .valorMinimo(new BigDecimal("100.00"))
                .valorMaximo(new BigDecimal("10000.00"))
                .ativo(true)
                .dataCriacao(LocalDateTime.of(2024, 1, 1, 10, 0))
                .build();
    }

    @Test
    void builderEGettersDevemPreencherCorretamente() {
        LocalDateTime dataCriacao = LocalDateTime.of(2024, 1, 1, 10, 0);

        ProdutoInvestimento p = ProdutoInvestimento.builder()
                .id(1L)
                .codigo("CDB100")
                .nome("CDB 100%")
                .tipo("RENDA_FIXA")
                .risco("BAIXO")
                .taxaAnual(new BigDecimal("0.12"))
                .liquidez("D+1")
                .prazoMinMeses(6)
                .prazoMaxMeses(24)
                .valorMinimo(new BigDecimal("100.00"))
                .valorMaximo(new BigDecimal("10000.00"))
                .ativo(true)
                .dataCriacao(dataCriacao)
                .build();

        assertEquals(1L, p.getId());
        assertEquals("CDB100", p.getCodigo());
        assertEquals("CDB 100%", p.getNome());
        assertEquals("RENDA_FIXA", p.getTipo());
        assertEquals("BAIXO", p.getRisco());
        assertEquals(new BigDecimal("0.12"), p.getTaxaAnual());
        assertEquals("D+1", p.getLiquidez());
        assertEquals(6, p.getPrazoMinMeses());
        assertEquals(24, p.getPrazoMaxMeses());
        assertEquals(new BigDecimal("100.00"), p.getValorMinimo());
        assertEquals(new BigDecimal("10000.00"), p.getValorMaximo());
        assertTrue(p.getAtivo());
        assertEquals(dataCriacao, p.getDataCriacao());
    }

    @Test
    void equalsEHashCodeDevemUsarIdECodigo() {
        ProdutoInvestimento p1 = criarBase(1L, "CDB100");
        ProdutoInvestimento p2 = criarBase(1L, "CDB100");
        ProdutoInvestimento p3 = criarBase(2L, "CDB200");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        assertNotEquals(p1, p3);
        assertNotEquals(p1.hashCode(), p3.hashCode());

        assertNotEquals(p1, null);
        assertNotEquals(p1, "outroTipo");
    }

    @Test
    void toStringDeveConterCamposPrincipais() {
        ProdutoInvestimento p = criarBase(1L, "CDB100");
        String str = p.toString();

        assertNotNull(str);
        assertTrue(str.contains("ProdutoInvestimento"));
        assertTrue(str.contains("CDB100"));
        assertTrue(str.contains("RENDA_FIXA"));
    }
}
