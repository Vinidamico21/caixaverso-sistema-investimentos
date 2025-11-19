package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.TelemetriaPeriodoDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaResponseDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaServicoDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TelemetriaResponseDTOTest {

    // --- Testes para Construtores e Builder ---

    @Test
    void testNoArgsConstructor() {
        TelemetriaResponseDTO dto = new TelemetriaResponseDTO();
        assertNotNull(dto);
        assertNull(dto.getServicos());
        assertNull(dto.getPeriodo());
    }

    @Test
    void testAllArgsConstructor() {
        List<TelemetriaServicoDTO> servicos = List.of(createServicoDTO("Teste", 10L, 100L));
        TelemetriaPeriodoDTO periodo = createPeriodoDTO(LocalDate.now(), LocalDate.now().plusDays(1));

        TelemetriaResponseDTO dto = new TelemetriaResponseDTO(servicos, periodo);

        assertEquals(servicos, dto.getServicos());
        assertEquals(periodo, dto.getPeriodo());
    }

    @Test
    void testBuilder() {
        List<TelemetriaServicoDTO> servicos = List.of(createServicoDTO("Builder", 5L, 50L));
        TelemetriaPeriodoDTO periodo = createPeriodoDTO(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 31));

        TelemetriaResponseDTO dto = TelemetriaResponseDTO.builder()
                .servicos(servicos)
                .periodo(periodo)
                .build();

        assertEquals(servicos, dto.getServicos());
        assertEquals(periodo, dto.getPeriodo());
    }

    // --- Testes para Getters e Setters ---

    @Test
    void testGettersAndSetters() {
        TelemetriaResponseDTO dto = new TelemetriaResponseDTO();

        List<TelemetriaServicoDTO> novosServicos = List.of(createServicoDTO("Setter", 1L, 10L));
        TelemetriaPeriodoDTO novoPeriodo = createPeriodoDTO(LocalDate.EPOCH, LocalDate.MAX);

        dto.setServicos(novosServicos);
        dto.setPeriodo(novoPeriodo);

        assertEquals(novosServicos, dto.getServicos());
        assertEquals(novoPeriodo, dto.getPeriodo());
    }

    // --- Testes para Equals e HashCode ---

    @Test
    void testEqualsAndHashCode() {
        List<TelemetriaServicoDTO> servicos1 = List.of(createServicoDTO("A", 10L, 100L));
        TelemetriaPeriodoDTO periodo1 = createPeriodoDTO(LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 10));

        List<TelemetriaServicoDTO> servicos2 = List.of(createServicoDTO("B", 20L, 200L));
        TelemetriaPeriodoDTO periodo2 = createPeriodoDTO(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 10));

        TelemetriaResponseDTO dto1 = new TelemetriaResponseDTO(servicos1, periodo1);
        TelemetriaResponseDTO dto2 = new TelemetriaResponseDTO(servicos1, periodo1); // Igual a dto1
        TelemetriaResponseDTO dto3 = new TelemetriaResponseDTO(servicos2, periodo1); // Serviços diferentes
        TelemetriaResponseDTO dto4 = new TelemetriaResponseDTO(servicos1, periodo2); // Período diferente
        TelemetriaResponseDTO dto5 = new TelemetriaResponseDTO(null, periodo1);      // Serviço nulo
        TelemetriaResponseDTO dto6 = new TelemetriaResponseDTO(servicos1, null);      // Período nulo

        // Testes de igualdade
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1, dto4);
        assertNotEquals(dto1, dto5);
        assertNotEquals(dto1, dto6);
        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());

        // Testes de hashCode (objetos iguais devem ter o mesmo hashCode)
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testEqualsAndHashCodeWithNulls() {
        TelemetriaResponseDTO dto1 = new TelemetriaResponseDTO(null, null);
        TelemetriaResponseDTO dto2 = new TelemetriaResponseDTO(null, null);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }


    // --- Teste para toString ---

    @Test
    void testToString() {
        List<TelemetriaServicoDTO> servicos = List.of(createServicoDTO("ToString", 77L, 300L));
        TelemetriaPeriodoDTO periodo = createPeriodoDTO(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 1, 5));

        TelemetriaResponseDTO dto = new TelemetriaResponseDTO(servicos, periodo);
        String result = dto.toString();

        // Verifica se a string contém as partes esperadas
        assertTrue(result.contains("TelemetriaResponseDTO{"));
        assertTrue(result.contains("servicos=" + servicos));
        assertTrue(result.contains("periodo=" + periodo));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testToStringWithNulls() {
        TelemetriaResponseDTO dto = new TelemetriaResponseDTO(null, null);
        String result = dto.toString();

        assertTrue(result.contains("TelemetriaResponseDTO{"));
        assertTrue(result.contains("servicos=null"));
        assertTrue(result.contains("periodo=null"));
        assertTrue(result.endsWith("}"));
    }


    // --- Métodos Auxiliares para Criar Objetos de Teste ---

    private TelemetriaServicoDTO createServicoDTO(String nome, Long quantidade, Long tempo) {
        return TelemetriaServicoDTO.builder()
                .nome(nome)
                .quantidadeChamadas(quantidade)
                .mediaTempoRespostaMs(tempo)
                .build();
    }

    private TelemetriaPeriodoDTO createPeriodoDTO(LocalDate inicio, LocalDate fim) {
        return TelemetriaPeriodoDTO.builder()
                .inicio(inicio)
                .fim(fim)
                .build();
    }
}