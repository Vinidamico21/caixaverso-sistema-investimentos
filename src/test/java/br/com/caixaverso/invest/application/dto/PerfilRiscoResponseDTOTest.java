package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.PerfilRiscoResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerfilRiscoResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        PerfilRiscoResponseDTO dto = new PerfilRiscoResponseDTO();

        assertNotNull(dto);
        assertNull(dto.getClienteId());
        assertNull(dto.getPerfil());
        assertNull(dto.getPontuacao());
        assertNull(dto.getDescricao());
    }

    @Test
    void testAllArgsConstructor() {
        PerfilRiscoResponseDTO dto = new PerfilRiscoResponseDTO(
                1L,
                "Moderado",
                "75",
                "Perfil equilibrado com tolerância média ao risco"
        );

        assertEquals(1L, dto.getClienteId());
        assertEquals("Moderado", dto.getPerfil());
        assertEquals("75", dto.getPontuacao());
        assertEquals("Perfil equilibrado com tolerância média ao risco", dto.getDescricao());
    }

    @Test
    void testBuilder() {
        PerfilRiscoResponseDTO dto = PerfilRiscoResponseDTO.builder()
                .clienteId(22L)
                .perfil("Arrojado")
                .pontuacao("92")
                .descricao("Alta tolerância ao risco")
                .build();

        assertEquals(22L, dto.getClienteId());
        assertEquals("Arrojado", dto.getPerfil());
        assertEquals("92", dto.getPontuacao());
        assertEquals("Alta tolerância ao risco", dto.getDescricao());
    }

    @Test
    void testSettersAndGetters() {
        PerfilRiscoResponseDTO dto = new PerfilRiscoResponseDTO();

        dto.setClienteId(7L);
        dto.setPerfil("Conservador");
        dto.setPontuacao("42");
        dto.setDescricao("Baixa tolerância a risco");

        assertEquals(7L, dto.getClienteId());
        assertEquals("Conservador", dto.getPerfil());
        assertEquals("42", dto.getPontuacao());
        assertEquals("Baixa tolerância a risco", dto.getDescricao());
    }

    @Test
    void testEqualsAndHashCode() {
        PerfilRiscoResponseDTO dto1 = new PerfilRiscoResponseDTO(
                10L, "Moderado", "70", "Equilibrado"
        );

        PerfilRiscoResponseDTO dto2 = new PerfilRiscoResponseDTO(
                10L, "Moderado", "70", "Equilibrado"
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PerfilRiscoResponseDTO dto = PerfilRiscoResponseDTO.builder()
                .clienteId(99L)
                .perfil("Arrojado")
                .pontuacao("95")
                .descricao("Busca alta rentabilidade com risco elevado")
                .build();

        String texto = dto.toString();

        assertTrue(texto.contains("99"));
        assertTrue(texto.contains("Arrojado"));
        assertTrue(texto.contains("95"));
        assertTrue(texto.contains("Busca alta rentabilidade"));
    }
}

