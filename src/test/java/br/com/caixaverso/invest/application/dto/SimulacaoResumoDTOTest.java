package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.SimulacaoResumoDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class SimulacaoResumoDTOTest {

    @Test
    void testNoArgsConstructor() {
        SimulacaoResumoDTO dto = new SimulacaoResumoDTO();

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getClienteId());
        assertNull(dto.getProduto());
        assertNull(dto.getValorInvestido());
        assertNull(dto.getValorFinal());
        assertNull(dto.getPrazoMeses());
        assertNull(dto.getDataSimulacao());
    }

    @Test
    void testAllArgsConstructor() {
        OffsetDateTime date = OffsetDateTime.of(
                2025, 1, 10, 15, 30, 0, 0, ZoneOffset.UTC
        );

        SimulacaoResumoDTO dto = new SimulacaoResumoDTO(
                1L,
                101L,
                "CDB",
                new BigDecimal("1000"),
                new BigDecimal("1200"),
                12,
                date
        );

        assertEquals(1L, dto.getId());
        assertEquals(101L, dto.getClienteId());
        assertEquals("CDB", dto.getProduto());
        assertEquals(new BigDecimal("1000"), dto.getValorInvestido());
        assertEquals(new BigDecimal("1200"), dto.getValorFinal());
        assertEquals(12, dto.getPrazoMeses());
        assertEquals(date, dto.getDataSimulacao());
    }

    @Test
    void testBuilder() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        SimulacaoResumoDTO dto = SimulacaoResumoDTO.builder()
                .id(50L)
                .clienteId(500L)
                .produto("Tesouro Selic")
                .valorInvestido(new BigDecimal("2000"))
                .valorFinal(new BigDecimal("2120"))
                .prazoMeses(6)
                .dataSimulacao(now)
                .build();

        assertEquals(50L, dto.getId());
        assertEquals(500L, dto.getClienteId());
        assertEquals("Tesouro Selic", dto.getProduto());
        assertEquals(new BigDecimal("2000"), dto.getValorInvestido());
        assertEquals(new BigDecimal("2120"), dto.getValorFinal());
        assertEquals(6, dto.getPrazoMeses());
        assertEquals(now, dto.getDataSimulacao());
    }

    @Test
    void testGettersAndSetters() {
        SimulacaoResumoDTO dto = new SimulacaoResumoDTO();

        OffsetDateTime date = OffsetDateTime.now();

        dto.setId(10L);
        dto.setClienteId(44L);
        dto.setProduto("FII XPTO11");
        dto.setValorInvestido(new BigDecimal("5000"));
        dto.setValorFinal(new BigDecimal("5800"));
        dto.setPrazoMeses(18);
        dto.setDataSimulacao(date);

        assertEquals(10L, dto.getId());
        assertEquals(44L, dto.getClienteId());
        assertEquals("FII XPTO11", dto.getProduto());
        assertEquals(new BigDecimal("5000"), dto.getValorInvestido());
        assertEquals(new BigDecimal("5800"), dto.getValorFinal());
        assertEquals(18, dto.getPrazoMeses());
        assertEquals(date, dto.getDataSimulacao());
    }

    @Test
    void testEqualsAndHashCode() {
        OffsetDateTime date = OffsetDateTime.now();

        SimulacaoResumoDTO dto1 = new SimulacaoResumoDTO(
                1L, 2L, "CDB",
                new BigDecimal("100"),
                new BigDecimal("110"),
                3,
                date
        );

        SimulacaoResumoDTO dto2 = new SimulacaoResumoDTO(
                1L, 2L, "CDB",
                new BigDecimal("100"),
                new BigDecimal("110"),
                3,
                date
        );

        SimulacaoResumoDTO dto3 = new SimulacaoResumoDTO(
                2L, 3L, "LCA",
                new BigDecimal("200"),
                new BigDecimal("220"),
                6,
                date.plusDays(1)
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
        SimulacaoResumoDTO dto1 = new SimulacaoResumoDTO();
        SimulacaoResumoDTO dto2 = new SimulacaoResumoDTO();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        dto1.setId(null);
        dto1.setClienteId(null);
        dto1.setProduto(null);
        dto1.setValorInvestido(null);
        dto1.setValorFinal(null);
        dto1.setPrazoMeses(null);
        dto1.setDataSimulacao(null);

        dto2.setId(null);
        dto2.setClienteId(null);
        dto2.setProduto(null);
        dto2.setValorInvestido(null);
        dto2.setValorFinal(null);
        dto2.setPrazoMeses(null);
        dto2.setDataSimulacao(null);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        OffsetDateTime date = OffsetDateTime.of(
                2026, 5, 1, 10, 0, 0, 0, ZoneOffset.UTC
        );

        SimulacaoResumoDTO dto = SimulacaoResumoDTO.builder()
                .id(999L)
                .clienteId(111L)
                .produto("LCI Banco X")
                .valorInvestido(new BigDecimal("3000"))
                .valorFinal(new BigDecimal("3300"))
                .prazoMeses(9)
                .dataSimulacao(date)
                .build();

        String result = dto.toString();

        assertTrue(result.contains("SimulacaoResumoDTO{"));
        assertTrue(result.contains("id=999"));
        assertTrue(result.contains("clienteId=111"));
        assertTrue(result.contains("produto='LCI Banco X'"));
        assertTrue(result.contains("valorInvestido=3000"));
        assertTrue(result.contains("valorFinal=3300"));
        assertTrue(result.contains("prazoMeses=9"));
        assertTrue(result.contains("dataSimulacao=2026-05-01T10:00Z"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testHashCodeConsistency() {
        SimulacaoResumoDTO dto = new SimulacaoResumoDTO(
                1L, 2L, "Produto",
                new BigDecimal("1000"),
                new BigDecimal("1100"),
                12,
                OffsetDateTime.now()
        );

        int hashCode1 = dto.hashCode();
        int hashCode2 = dto.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testPartialNullValues() {
        SimulacaoResumoDTO dto1 = new SimulacaoResumoDTO(
                1L, null, "Produto", null, new BigDecimal("1100"), 12, null
        );

        SimulacaoResumoDTO dto2 = new SimulacaoResumoDTO(
                1L, null, "Produto", null, new BigDecimal("1100"), 12, null
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testDifferentOffsetDateTime() {
        OffsetDateTime date1 = OffsetDateTime.of(2025, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime date2 = OffsetDateTime.of(2025, 1, 1, 10, 0, 0, 0, ZoneOffset.ofHours(-3));

        SimulacaoResumoDTO dto1 = new SimulacaoResumoDTO(
                1L, 2L, "Produto", new BigDecimal("1000"), new BigDecimal("1100"), 12, date1
        );

        SimulacaoResumoDTO dto2 = new SimulacaoResumoDTO(
                1L, 2L, "Produto", new BigDecimal("1000"), new BigDecimal("1100"), 12, date2
        );

        // OffsetDateTime com diferentes zonas devem ser diferentes
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }
}