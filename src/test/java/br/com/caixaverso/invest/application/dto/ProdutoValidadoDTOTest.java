package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.ProdutoValidadoDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoValidadoDTOTest {

    @Test
    void testNoArgsConstructor() {
        ProdutoValidadoDTO dto = new ProdutoValidadoDTO();

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getNome());
        assertNull(dto.getTipo());
        assertNull(dto.getRentabilidade());
        assertNull(dto.getRisco());
    }

    @Test
    void testAllArgsConstructor() {
        ProdutoValidadoDTO dto = new ProdutoValidadoDTO(
                1L,
                "Tesouro Prefixado",
                "Renda Fixa",
                new BigDecimal("0.1025"),
                "Médio"
        );

        assertEquals(1L, dto.getId());
        assertEquals("Tesouro Prefixado", dto.getNome());
        assertEquals("Renda Fixa", dto.getTipo());
        assertEquals(new BigDecimal("0.1025"), dto.getRentabilidade());
        assertEquals("Médio", dto.getRisco());
    }

    @Test
    void testBuilder() {
        ProdutoValidadoDTO dto = ProdutoValidadoDTO.builder()
                .id(50L)
                .nome("CDB Banco X")
                .tipo("Renda Fixa")
                .rentabilidade(new BigDecimal("0.13"))
                .risco("Baixo")
                .build();

        assertEquals(50L, dto.getId());
        assertEquals("CDB Banco X", dto.getNome());
        assertEquals("Renda Fixa", dto.getTipo());
        assertEquals(new BigDecimal("0.13"), dto.getRentabilidade());
        assertEquals("Baixo", dto.getRisco());
    }

    @Test
    void testGettersAndSetters() {
        ProdutoValidadoDTO dto = new ProdutoValidadoDTO();

        dto.setId(77L);
        dto.setNome("LCI Santander");
        dto.setTipo("Renda Fixa");
        dto.setRentabilidade(new BigDecimal("0.08"));
        dto.setRisco("Baixo");

        assertEquals(77L, dto.getId());
        assertEquals("LCI Santander", dto.getNome());
        assertEquals("Renda Fixa", dto.getTipo());
        assertEquals(new BigDecimal("0.08"), dto.getRentabilidade());
        assertEquals("Baixo", dto.getRisco());
    }

    @Test
    void testEqualsAndHashCode() {
        ProdutoValidadoDTO dto1 = new ProdutoValidadoDTO(
                10L, "Produto X", "RF",
                new BigDecimal("0.10"), "Baixo"
        );

        ProdutoValidadoDTO dto2 = new ProdutoValidadoDTO(
                10L, "Produto X", "RF",
                new BigDecimal("0.10"), "Baixo"
        );

        ProdutoValidadoDTO dto3 = new ProdutoValidadoDTO(
                20L, "Produto Y", "RV",
                new BigDecimal("0.15"), "Alto"
        );

        // Teste igualdade
        assertEquals(dto1, dto2);
        assertEquals(dto1, dto1); // mesma instância
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());

        // Teste hashCode
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testEqualsWithNullValues() {
        ProdutoValidadoDTO dto1 = new ProdutoValidadoDTO();
        ProdutoValidadoDTO dto2 = new ProdutoValidadoDTO();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        dto1.setId(null);
        dto1.setNome(null);
        dto1.setTipo(null);
        dto1.setRentabilidade(null);
        dto1.setRisco(null);

        dto2.setId(null);
        dto2.setNome(null);
        dto2.setTipo(null);
        dto2.setRentabilidade(null);
        dto2.setRisco(null);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProdutoValidadoDTO dto = ProdutoValidadoDTO.builder()
                .id(123L)
                .nome("ETF ABCD11")
                .tipo("ETF")
                .rentabilidade(new BigDecimal("0.17"))
                .risco("Alto")
                .build();

        String result = dto.toString();

        assertTrue(result.contains("ProdutoValidadoDTO{"));
        assertTrue(result.contains("id=123"));
        assertTrue(result.contains("nome='ETF ABCD11'"));
        assertTrue(result.contains("tipo='ETF'"));
        assertTrue(result.contains("rentabilidade=0.17"));
        assertTrue(result.contains("risco='Alto'"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testHashCodeConsistency() {
        ProdutoValidadoDTO dto = new ProdutoValidadoDTO(
                1L, "Teste", "Tipo", new BigDecimal("0.12"), "Risco"
        );

        int hashCode1 = dto.hashCode();
        int hashCode2 = dto.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testPartialNullValues() {
        ProdutoValidadoDTO dto1 = new ProdutoValidadoDTO(
                1L, null, "Tipo", null, "Risco"
        );

        ProdutoValidadoDTO dto2 = new ProdutoValidadoDTO(
                1L, null, "Tipo", null, "Risco"
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testDifferentRentabilidadePrecision() {
        ProdutoValidadoDTO dto1 = new ProdutoValidadoDTO(1L, "Produto", "Tipo", new BigDecimal("0.1000"), "Risco");
        ProdutoValidadoDTO dto2 = new ProdutoValidadoDTO(1L, "Produto", "Tipo", new BigDecimal("0.10"), "Risco");

        // Use compareTo para BigDecimal em vez de equals
        assertEquals(0, dto1.getRentabilidade().compareTo(dto2.getRentabilidade()));
        assertEquals(dto1, dto2);
    }
}