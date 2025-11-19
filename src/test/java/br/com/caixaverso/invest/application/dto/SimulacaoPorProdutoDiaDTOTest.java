package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.SimulacaoPorProdutoDiaDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SimulacaoPorProdutoDiaDTOTest {

    @Test
    void testNoArgsConstructor() {
        SimulacaoPorProdutoDiaDTO dto = new SimulacaoPorProdutoDiaDTO();

        assertNotNull(dto);
        assertNull(dto.getProduto());
        assertNull(dto.getData());
        assertNull(dto.getQuantidadeSimulacoes());
        assertNull(dto.getMediaValorFinal());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDate date = LocalDate.of(2025, 1, 15);

        SimulacaoPorProdutoDiaDTO dto = new SimulacaoPorProdutoDiaDTO(
                "CDB",
                date,
                10L,
                new BigDecimal("1500.75")
        );

        assertEquals("CDB", dto.getProduto());
        assertEquals(date, dto.getData());
        assertEquals(10L, dto.getQuantidadeSimulacoes());
        assertEquals(new BigDecimal("1500.75"), dto.getMediaValorFinal());
    }

    @Test
    void testBuilder() {
        LocalDate hoje = LocalDate.now();

        SimulacaoPorProdutoDiaDTO dto = SimulacaoPorProdutoDiaDTO.builder()
                .produto("Tesouro Direto")
                .data(hoje)
                .quantidadeSimulacoes(25L)
                .mediaValorFinal(new BigDecimal("2000.00"))
                .build();

        assertEquals("Tesouro Direto", dto.getProduto());
        assertEquals(hoje, dto.getData());
        assertEquals(25L, dto.getQuantidadeSimulacoes());
        assertEquals(new BigDecimal("2000.00"), dto.getMediaValorFinal());
    }

    @Test
    void testGettersAndSetters() {
        SimulacaoPorProdutoDiaDTO dto = new SimulacaoPorProdutoDiaDTO();

        dto.setProduto("LCI");
        dto.setData(LocalDate.of(2024, 12, 20));
        dto.setQuantidadeSimulacoes(5L);
        dto.setMediaValorFinal(new BigDecimal("3000"));

        assertEquals("LCI", dto.getProduto());
        assertEquals(LocalDate.of(2024, 12, 20), dto.getData());
        assertEquals(5L, dto.getQuantidadeSimulacoes());
        assertEquals(new BigDecimal("3000"), dto.getMediaValorFinal());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDate data = LocalDate.of(2025, 3, 1);

        SimulacaoPorProdutoDiaDTO dto1 = new SimulacaoPorProdutoDiaDTO(
                "Fundo Imobiliário",
                data,
                12L,
                new BigDecimal("1800.50")
        );

        SimulacaoPorProdutoDiaDTO dto2 = new SimulacaoPorProdutoDiaDTO(
                "Fundo Imobiliário",
                data,
                12L,
                new BigDecimal("1800.50")
        );

        SimulacaoPorProdutoDiaDTO dto3 = new SimulacaoPorProdutoDiaDTO(
                "CDB",
                data.plusDays(1),
                8L,
                new BigDecimal("1200.00")
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
        SimulacaoPorProdutoDiaDTO dto1 = new SimulacaoPorProdutoDiaDTO();
        SimulacaoPorProdutoDiaDTO dto2 = new SimulacaoPorProdutoDiaDTO();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        dto1.setProduto(null);
        dto1.setData(null);
        dto1.setQuantidadeSimulacoes(null);
        dto1.setMediaValorFinal(null);

        dto2.setProduto(null);
        dto2.setData(null);
        dto2.setQuantidadeSimulacoes(null);
        dto2.setMediaValorFinal(null);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LocalDate date = LocalDate.of(2026, 6, 10);

        SimulacaoPorProdutoDiaDTO dto = SimulacaoPorProdutoDiaDTO.builder()
                .produto("ETF ABCD11")
                .data(date)
                .quantidadeSimulacoes(40L)
                .mediaValorFinal(new BigDecimal("5000"))
                .build();

        String result = dto.toString();

        assertTrue(result.contains("SimulacaoPorProdutoDiaDTO{"));
        assertTrue(result.contains("produto='ETF ABCD11'"));
        assertTrue(result.contains("data=2026-06-10"));
        assertTrue(result.contains("quantidadeSimulacoes=40"));
        assertTrue(result.contains("mediaValorFinal=5000"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testHashCodeConsistency() {
        SimulacaoPorProdutoDiaDTO dto = new SimulacaoPorProdutoDiaDTO(
                "Produto", LocalDate.now(), 15L, new BigDecimal("1000.00")
        );

        int hashCode1 = dto.hashCode();
        int hashCode2 = dto.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testPartialNullValues() {
        SimulacaoPorProdutoDiaDTO dto1 = new SimulacaoPorProdutoDiaDTO(
                "Produto", null, 10L, null
        );

        SimulacaoPorProdutoDiaDTO dto2 = new SimulacaoPorProdutoDiaDTO(
                "Produto", null, 10L, null
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testDifferentBigDecimalPrecision() {
        SimulacaoPorProdutoDiaDTO dto1 = new SimulacaoPorProdutoDiaDTO("Produto", LocalDate.now(), 10L, new BigDecimal("1000.0000"));
        SimulacaoPorProdutoDiaDTO dto2 = new SimulacaoPorProdutoDiaDTO("Produto", LocalDate.now(), 10L, new BigDecimal("1000.00"));

        assertEquals(0, dto1.getMediaValorFinal().compareTo(dto2.getMediaValorFinal()));
        assertEquals(dto1, dto2);
    }
}