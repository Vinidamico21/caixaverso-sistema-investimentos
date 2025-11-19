package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.InvestimentoResponseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InvestimentoResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        InvestimentoResponseDTO dto = new InvestimentoResponseDTO();

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getTipo());
        assertNull(dto.getValor());
        assertNull(dto.getRentabilidade());
        assertNull(dto.getData());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDate hoje = LocalDate.now();

        InvestimentoResponseDTO dto = new InvestimentoResponseDTO(
                1L,
                "CDB",
                new BigDecimal("1000.00"),
                new BigDecimal("0.12"),
                hoje
        );

        assertEquals(1L, dto.getId());
        assertEquals("CDB", dto.getTipo());
        assertEquals(new BigDecimal("1000.00"), dto.getValor());
        assertEquals(new BigDecimal("0.12"), dto.getRentabilidade());
        assertEquals(hoje, dto.getData());
    }

    @Test
    void testBuilder() {
        LocalDate ontem = LocalDate.now().minusDays(1);

        InvestimentoResponseDTO dto = InvestimentoResponseDTO.builder()
                .id(10L)
                .tipo("Tesouro Direto")
                .valor(new BigDecimal("2500.55"))
                .rentabilidade(new BigDecimal("0.08"))
                .data(ontem)
                .build();

        assertEquals(10L, dto.getId());
        assertEquals("Tesouro Direto", dto.getTipo());
        assertEquals(new BigDecimal("2500.55"), dto.getValor());
        assertEquals(new BigDecimal("0.08"), dto.getRentabilidade());
        assertEquals(ontem, dto.getData());
    }

    @Test
    void testGettersAndSetters() {
        InvestimentoResponseDTO dto = new InvestimentoResponseDTO();

        dto.setId(99L);
        dto.setTipo("LCI");
        dto.setValor(new BigDecimal("5000.00"));
        dto.setRentabilidade(new BigDecimal("0.10"));
        dto.setData(LocalDate.of(2025, 1, 1));

        assertEquals(99L, dto.getId());
        assertEquals("LCI", dto.getTipo());
        assertEquals(new BigDecimal("5000.00"), dto.getValor());
        assertEquals(new BigDecimal("0.10"), dto.getRentabilidade());
        assertEquals(LocalDate.of(2025, 1, 1), dto.getData());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDate date = LocalDate.now();

        InvestimentoResponseDTO dto1 = new InvestimentoResponseDTO(
                1L, "CDB",
                new BigDecimal("100"),
                new BigDecimal("0.1"),
                date
        );

        InvestimentoResponseDTO dto2 = new InvestimentoResponseDTO(
                1L, "CDB",
                new BigDecimal("100"),
                new BigDecimal("0.1"),
                date
        );

        InvestimentoResponseDTO dto3 = new InvestimentoResponseDTO(
                2L, "LCA",
                new BigDecimal("200"),
                new BigDecimal("0.2"),
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
        InvestimentoResponseDTO dto1 = new InvestimentoResponseDTO();
        InvestimentoResponseDTO dto2 = new InvestimentoResponseDTO();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        dto1.setId(null);
        dto1.setTipo(null);
        dto1.setValor(null);
        dto1.setRentabilidade(null);
        dto1.setData(null);

        dto2.setId(null);
        dto2.setTipo(null);
        dto2.setValor(null);
        dto2.setRentabilidade(null);
        dto2.setData(null);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LocalDate date = LocalDate.of(2025, 5, 10);

        InvestimentoResponseDTO dto = InvestimentoResponseDTO.builder()
                .id(5L)
                .tipo("Fundo Imobiliário")
                .valor(new BigDecimal("1500.00"))
                .rentabilidade(new BigDecimal("0.07"))
                .data(date)
                .build();

        String result = dto.toString();

        // Formato padrão do Lombok: InvestimentoResponseDTO(id=..., tipo=..., ...)
        assertTrue(result.startsWith("InvestimentoResponseDTO("));
        assertTrue(result.contains("id=5"));
        assertTrue(result.contains("tipo=Fundo Imobiliário"));
        assertTrue(result.contains("valor=1500.00"));
        assertTrue(result.contains("rentabilidade=0.07"));
        assertTrue(result.contains("data=2025-05-10"));
        assertTrue(result.endsWith(")"));
    }

    @Test
    void testHashCodeConsistency() {
        InvestimentoResponseDTO dto = new InvestimentoResponseDTO(
                1L, "CDB", new BigDecimal("1000.00"), new BigDecimal("0.12"), LocalDate.now()
        );

        int hashCode1 = dto.hashCode();
        int hashCode2 = dto.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testPartialNullValues() {
        InvestimentoResponseDTO dto1 = new InvestimentoResponseDTO(
                1L, null, new BigDecimal("1000.00"), null, LocalDate.now()
        );

        InvestimentoResponseDTO dto2 = new InvestimentoResponseDTO(
                1L, null, new BigDecimal("1000.00"), null, LocalDate.now()
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}
