package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.TelemetriaServicoDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TelemetriaServicoDTOTest {

    @Test
    void testNoArgsConstructor() {
        TelemetriaServicoDTO dto = new TelemetriaServicoDTO();

        assertNotNull(dto);
        assertNull(dto.getNome());
        assertNull(dto.getQuantidadeChamadas());
        assertNull(dto.getMediaTempoRespostaMs());
    }

    @Test
    void testAllArgsConstructor() {
        TelemetriaServicoDTO dto = new TelemetriaServicoDTO(
                "Simulacao",
                100L,
                150L
        );

        assertEquals("Simulacao", dto.getNome());
        assertEquals(100L, dto.getQuantidadeChamadas());
        assertEquals(150L, dto.getMediaTempoRespostaMs());
    }

    @Test
    void testBuilder() {
        TelemetriaServicoDTO dto = TelemetriaServicoDTO.builder()
                .nome("Recomendacao")
                .quantidadeChamadas(200L)
                .mediaTempoRespostaMs(300L)
                .build();

        assertEquals("Recomendacao", dto.getNome());
        assertEquals(200L, dto.getQuantidadeChamadas());
        assertEquals(300L, dto.getMediaTempoRespostaMs());
    }

    @Test
    void testGettersAndSetters() {
        TelemetriaServicoDTO dto = new TelemetriaServicoDTO();

        dto.setNome("API");
        dto.setQuantidadeChamadas(20L);
        dto.setMediaTempoRespostaMs(500L);

        assertEquals("API", dto.getNome());
        assertEquals(20L, dto.getQuantidadeChamadas());
        assertEquals(500L, dto.getMediaTempoRespostaMs());
    }

    @Test
    void testEqualsAndHashCode() {
        TelemetriaServicoDTO dto1 = new TelemetriaServicoDTO(
                "X", 10L, 100L
        );

        TelemetriaServicoDTO dto2 = new TelemetriaServicoDTO(
                "X", 10L, 100L
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TelemetriaServicoDTO dto = TelemetriaServicoDTO.builder()
                .nome("Servico")
                .quantidadeChamadas(77L)
                .mediaTempoRespostaMs(250L)
                .build();

        String str = dto.toString();

        assertTrue(str.contains("Servico"));
        assertTrue(str.contains("77"));
        assertTrue(str.contains("250"));
    }
}