package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.AuthResponseDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        AuthResponseDTO dto = new AuthResponseDTO();

        assertNotNull(dto);
        assertNull(dto.getToken());
        assertNull(dto.getTokenType());
        assertEquals(0L, dto.getExpiresIn());
    }

    @Test
    void testAllArgsConstructor() {
        AuthResponseDTO dto = new AuthResponseDTO("abc123", "Bearer", 3600L);

        assertEquals("abc123", dto.getToken());
        assertEquals("Bearer", dto.getTokenType());
        assertEquals(3600L, dto.getExpiresIn());
    }

    @Test
    void testBuilder() {
        AuthResponseDTO dto = AuthResponseDTO.builder()
                .token("jwt-token")
                .tokenType("Bearer")
                .expiresIn(7200L)
                .build();

        assertEquals("jwt-token", dto.getToken());
        assertEquals("Bearer", dto.getTokenType());
        assertEquals(7200L, dto.getExpiresIn());
    }

    @Test
    void testGettersAndSetters() {
        AuthResponseDTO dto = new AuthResponseDTO();

        dto.setToken("tk");
        dto.setTokenType("API");
        dto.setExpiresIn(999L);

        assertEquals("tk", dto.getToken());
        assertEquals("API", dto.getTokenType());
        assertEquals(999L, dto.getExpiresIn());
    }

    @Test
    void testEqualsAndHashCode() {
        AuthResponseDTO dto1 = new AuthResponseDTO("t1", "Bearer", 1000L);
        AuthResponseDTO dto2 = new AuthResponseDTO("t1", "Bearer", 1000L);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AuthResponseDTO dto = new AuthResponseDTO("t123", "Bearer", 5000L);

        String str = dto.toString();

        assertTrue(str.contains("t123"));
        assertTrue(str.contains("Bearer"));
        assertTrue(str.contains("5000"));
    }
}

