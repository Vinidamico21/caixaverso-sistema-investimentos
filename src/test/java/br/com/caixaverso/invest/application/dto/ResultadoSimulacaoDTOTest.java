package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.ResultadoSimulacaoDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ResultadoSimulacaoDTOTest {

    @Test
    void testNoArgsConstructor() {
        ResultadoSimulacaoDTO dto = new ResultadoSimulacaoDTO();

        assertNotNull(dto);
        assertNull(dto.getValorFinal());
        assertNull(dto.getRentabilidadeEfetiva());
        assertNull(dto.getPrazoMeses());
    }

    @Test
    void testAllArgsConstructor() {
        ResultadoSimulacaoDTO dto = new ResultadoSimulacaoDTO(
                new BigDecimal("1500.75"),
                new BigDecimal("0.085"),
                12
        );

        assertEquals(new BigDecimal("1500.75"), dto.getValorFinal());
        assertEquals(new BigDecimal("0.085"), dto.getRentabilidadeEfetiva());
        assertEquals(12, dto.getPrazoMeses());
    }

    @Test
    void testBuilder() {
        ResultadoSimulacaoDTO dto = ResultadoSimulacaoDTO.builder()
                .valorFinal(new BigDecimal("2000.00"))
                .rentabilidadeEfetiva(new BigDecimal("0.12"))
                .prazoMeses(24)
                .build();

        assertEquals(new BigDecimal("2000.00"), dto.getValorFinal());
        assertEquals(new BigDecimal("0.12"), dto.getRentabilidadeEfetiva());
        assertEquals(24, dto.getPrazoMeses());
    }

    @Test
    void testGettersAndSetters() {
        ResultadoSimulacaoDTO dto = new ResultadoSimulacaoDTO();

        dto.setValorFinal(new BigDecimal("3000"));
        dto.setRentabilidadeEfetiva(new BigDecimal("0.10"));
        dto.setPrazoMeses(36);

        assertEquals(new BigDecimal("3000"), dto.getValorFinal());
        assertEquals(new BigDecimal("0.10"), dto.getRentabilidadeEfetiva());
        assertEquals(36, dto.getPrazoMeses());
    }

    @Test
    void testEqualsAndHashCode() {
        ResultadoSimulacaoDTO dto1 = new ResultadoSimulacaoDTO(
                new BigDecimal("1000"),
                new BigDecimal("0.05"),
                6
        );

        ResultadoSimulacaoDTO dto2 = new ResultadoSimulacaoDTO(
                new BigDecimal("1000"),
                new BigDecimal("0.05"),
                6
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ResultadoSimulacaoDTO dto = ResultadoSimulacaoDTO.builder()
                .valorFinal(new BigDecimal("5000"))
                .rentabilidadeEfetiva(new BigDecimal("0.14"))
                .prazoMeses(48)
                .build();

        String str = dto.toString();

        assertTrue(str.contains("5000"));
        assertTrue(str.contains("0.14"));
        assertTrue(str.contains("48"));
    }
}