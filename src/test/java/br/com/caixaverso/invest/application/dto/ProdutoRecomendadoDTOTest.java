package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.ProdutoRecomendadoDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoRecomendadoDTOTest {

    @Test
    void testNoArgsConstructor() {
        ProdutoRecomendadoDTO dto = new ProdutoRecomendadoDTO();

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getNome());
        assertNull(dto.getTipo());
        assertNull(dto.getRentabilidade());
        assertNull(dto.getRisco());
    }

    @Test
    void testAllArgsConstructor() {
        ProdutoRecomendadoDTO dto = new ProdutoRecomendadoDTO(
                1L,
                "Tesouro Selic",
                "Renda Fixa",
                new BigDecimal("0.115"),
                "Baixo"
        );

        assertEquals(1L, dto.getId());
        assertEquals("Tesouro Selic", dto.getNome());
        assertEquals("Renda Fixa", dto.getTipo());
        assertEquals(new BigDecimal("0.115"), dto.getRentabilidade());
        assertEquals("Baixo", dto.getRisco());
    }

    @Test
    void testBuilder() {
        ProdutoRecomendadoDTO dto = ProdutoRecomendadoDTO.builder()
                .id(55L)
                .nome("FII XPTO11")
                .tipo("Fundo Imobiliário")
                .rentabilidade(new BigDecimal("0.09"))
                .risco("Médio")
                .build();

        assertEquals(55L, dto.getId());
        assertEquals("FII XPTO11", dto.getNome());
        assertEquals("Fundo Imobiliário", dto.getTipo());
        assertEquals(new BigDecimal("0.09"), dto.getRentabilidade());
        assertEquals("Médio", dto.getRisco());
    }

    @Test
    void testSettersAndGetters() {
        ProdutoRecomendadoDTO dto = new ProdutoRecomendadoDTO();

        dto.setId(99L);
        dto.setNome("CDB Premium");
        dto.setTipo("Renda Fixa");
        dto.setRentabilidade(new BigDecimal("0.12"));
        dto.setRisco("Baixo");

        assertEquals(99L, dto.getId());
        assertEquals("CDB Premium", dto.getNome());
        assertEquals("Renda Fixa", dto.getTipo());
        assertEquals(new BigDecimal("0.12"), dto.getRentabilidade());
        assertEquals("Baixo", dto.getRisco());
    }

    @Test
    void testEqualsAndHashCode() {
        ProdutoRecomendadoDTO dto1 = new ProdutoRecomendadoDTO(
                10L, "ProdutoX", "RF",
                new BigDecimal("0.10"), "Baixo"
        );

        ProdutoRecomendadoDTO dto2 = new ProdutoRecomendadoDTO(
                10L, "ProdutoX", "RF",
                new BigDecimal("0.10"), "Baixo"
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProdutoRecomendadoDTO dto = ProdutoRecomendadoDTO.builder()
                .id(202L)
                .nome("ETF ABCD11")
                .tipo("ETF")
                .rentabilidade(new BigDecimal("0.18"))
                .risco("Alto")
                .build();

        String text = dto.toString();

        assertTrue(text.contains("202"));
        assertTrue(text.contains("ETF ABCD11"));
        assertTrue(text.contains("ETF"));
        assertTrue(text.contains("0.18"));
        assertTrue(text.contains("Alto"));
    }
}