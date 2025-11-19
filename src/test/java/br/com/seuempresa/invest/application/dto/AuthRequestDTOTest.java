package br.com.seuempresa.invest.application.dto;

import br.com.caixaverso.invest.application.dto.AuthRequestDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthRequestDTOTest {

    @Test
    void testNoArgsConstructor() {
        AuthRequestDTO dto = new AuthRequestDTO();
        assertNotNull(dto);
        assertNull(dto.getUsername());
        assertNull(dto.getPassword());
    }

    @Test
    void testAllArgsConstructor() {
        AuthRequestDTO dto = new AuthRequestDTO("user123", "pass123");

        assertEquals("user123", dto.getUsername());
        assertEquals("pass123", dto.getPassword());
    }

    @Test
    void testBuilder() {
        AuthRequestDTO dto = AuthRequestDTO.builder()
                .username("admin")
                .password("123456")
                .build();

        assertEquals("admin", dto.getUsername());
        assertEquals("123456", dto.getPassword());
    }

    @Test
    void testSettersAndGetters() {
        AuthRequestDTO dto = new AuthRequestDTO();

        dto.setUsername("john");
        dto.setPassword("secret");

        assertEquals("john", dto.getUsername());
        assertEquals("secret", dto.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        AuthRequestDTO dto1 = new AuthRequestDTO("user", "123");
        AuthRequestDTO dto2 = new AuthRequestDTO("user", "123");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AuthRequestDTO dto = new AuthRequestDTO("abc", "xyz");

        String str = dto.toString();

        assertTrue(str.contains("abc"));
        assertTrue(str.contains("xyz"));
    }
}
