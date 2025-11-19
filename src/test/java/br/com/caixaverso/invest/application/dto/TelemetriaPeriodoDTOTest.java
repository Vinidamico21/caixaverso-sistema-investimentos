package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.TelemetriaPeriodoDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TelemetriaPeriodoDTOTest {

    @Test
    void testNoArgsConstructor() {
        TelemetriaPeriodoDTO dto = new TelemetriaPeriodoDTO();

        assertNotNull(dto);
        assertNull(dto.getInicio());
        assertNull(dto.getFim());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fim = LocalDate.of(2025, 1, 31);

        TelemetriaPeriodoDTO dto = new TelemetriaPeriodoDTO(inicio, fim);

        assertEquals(inicio, dto.getInicio());
        assertEquals(fim, dto.getFim());
    }

    @Test
    void testBuilder() {
        LocalDate inicio = LocalDate.of(2024, 12, 5);
        LocalDate fim = LocalDate.of(2024, 12, 20);

        TelemetriaPeriodoDTO dto = TelemetriaPeriodoDTO.builder()
                .inicio(inicio)
                .fim(fim)
                .build();

        assertEquals(inicio, dto.getInicio());
        assertEquals(fim, dto.getFim());
    }

    @Test
    void testGettersAndSetters() {
        TelemetriaPeriodoDTO dto = new TelemetriaPeriodoDTO();

        LocalDate inicio = LocalDate.of(2023, 6, 1);
        LocalDate fim = LocalDate.of(2023, 6, 30);

        dto.setInicio(inicio);
        dto.setFim(fim);

        assertEquals(inicio, dto.getInicio());
        assertEquals(fim, dto.getFim());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDate inicio = LocalDate.of(2024, 2, 1);
        LocalDate fim = LocalDate.of(2024, 2, 29);

        TelemetriaPeriodoDTO dto1 = new TelemetriaPeriodoDTO(inicio, fim);
        TelemetriaPeriodoDTO dto2 = new TelemetriaPeriodoDTO(inicio, fim);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LocalDate inicio = LocalDate.of(2030, 1, 1);
        LocalDate fim = LocalDate.of(2030, 1, 10);

        TelemetriaPeriodoDTO dto = TelemetriaPeriodoDTO.builder()
                .inicio(inicio)
                .fim(fim)
                .build();

        String str = dto.toString();

        assertTrue(str.contains("2030-01-01"));
        assertTrue(str.contains("2030-01-10"));
    }
}