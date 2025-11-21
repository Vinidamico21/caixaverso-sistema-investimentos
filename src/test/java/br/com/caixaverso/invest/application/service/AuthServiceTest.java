package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.AuthRequestDTO;
import br.com.caixaverso.invest.application.dto.AuthResponseDTO;
import br.com.caixaverso.invest.application.port.out.TentativaLoginPort; 
import br.com.caixaverso.invest.infra.exception.BusinessException;
import br.com.caixaverso.invest.infra.exception.UnauthorizedException;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private TentativaLoginPort tentativaLoginMock;

    @BeforeEach
    void setup() {
        authService = new AuthService();
        authService.expirationSeconds = 3600;

        tentativaLoginMock = mock(TentativaLoginPort.class);
        when(tentativaLoginMock.estaBloqueado(anyString())).thenReturn(false);

        authService.tentativaLoginPort = tentativaLoginMock;
    }

    @Test
    void testAutenticarComAdmin_RetornaTokenValido() {
        try (MockedStatic<Jwt> jwtMock = mockStatic(Jwt.class)) {
            JwtClaimsBuilder builderMock = mock(JwtClaimsBuilder.class);

            when(Jwt.issuer("caixaverso-investimentos")).thenReturn(builderMock);
            when(builderMock.subject(anyString())).thenReturn(builderMock);
            when(builderMock.upn(anyString())).thenReturn(builderMock);
            when(builderMock.groups(anySet())).thenReturn(builderMock);
            when(builderMock.issuedAt(any(Instant.class))).thenReturn(builderMock);
            when(builderMock.expiresAt(any(Instant.class))).thenReturn(builderMock);
            when(builderMock.sign()).thenReturn("TOKEN_SIMULADO");

            AuthRequestDTO request = AuthRequestDTO.builder()
                    .username("admin")
                    .password("admin@123teste")
                    .build();

            AuthResponseDTO response = authService.autenticar(request);

            assertEquals("TOKEN_SIMULADO", response.getToken());
        }
    }

    @Test
    void testAutenticarComUser_RetornaToken() {
        try (MockedStatic<Jwt> jwtMock = mockStatic(Jwt.class)) {
            JwtClaimsBuilder builderMock = mock(JwtClaimsBuilder.class);

            when(Jwt.issuer("caixaverso-investimentos")).thenReturn(builderMock);
            when(builderMock.subject(anyString())).thenReturn(builderMock);
            when(builderMock.upn(anyString())).thenReturn(builderMock);
            when(builderMock.groups(anySet())).thenReturn(builderMock);
            when(builderMock.issuedAt(any(Instant.class))).thenReturn(builderMock);
            when(builderMock.expiresAt(any(Instant.class))).thenReturn(builderMock);
            when(builderMock.sign()).thenReturn("TOKEN_USER");

            AuthRequestDTO request = AuthRequestDTO.builder()
                    .username("user")
                    .password("user@123teste")
                    .build();

            AuthResponseDTO response = authService.autenticar(request);

            assertEquals("TOKEN_USER", response.getToken());
        }
    }

    @Test
    void testAutenticarRequestNulo_LancaBusinessException() {
        assertThrows(BusinessException.class, () -> authService.autenticar(null));
    }

    @Test
    void testAutenticarSemUsername_LancaBusinessException() {
        AuthRequestDTO dto = new AuthRequestDTO(null, "abc");
        assertThrows(BusinessException.class, () -> authService.autenticar(dto));
    }

    @Test
    void testAutenticarCredenciaisInvalidas_LancaUnauthorized() {
        AuthRequestDTO dto = new AuthRequestDTO("wrong", "credentials");
        assertThrows(UnauthorizedException.class, () -> authService.autenticar(dto));
    }

    @Test
    void testGerarToken() {
        try (MockedStatic<Jwt> jwtMock = mockStatic(Jwt.class)) {
            JwtClaimsBuilder builderMock = mock(JwtClaimsBuilder.class);

            when(Jwt.issuer("caixaverso-investimentos")).thenReturn(builderMock);
            when(builderMock.subject("user")).thenReturn(builderMock);
            when(builderMock.upn("user")).thenReturn(builderMock);
            when(builderMock.groups(Set.of("USER"))).thenReturn(builderMock);
            when(builderMock.issuedAt(any(Instant.class))).thenReturn(builderMock);
            when(builderMock.expiresAt(any(Instant.class))).thenReturn(builderMock);
            when(builderMock.sign()).thenReturn("TOKENTESTE");

            String token = authService.gerarToken("user", Set.of("USER"));

            assertEquals("TOKENTESTE", token);
        }
    }

    @Test
    void testAutenticarComEspacosEMaiusculas_NormalizaEFunciona() {
        try (MockedStatic<Jwt> jwtMock = mockStatic(Jwt.class)) {
            JwtClaimsBuilder builderMock = mock(JwtClaimsBuilder.class);

            when(Jwt.issuer("caixaverso-investimentos")).thenReturn(builderMock);
            when(builderMock.subject(anyString())).thenReturn(builderMock);
            when(builderMock.upn(anyString())).thenReturn(builderMock);
            when(builderMock.groups(anySet())).thenReturn(builderMock);
            when(builderMock.issuedAt(any(Instant.class))).thenReturn(builderMock);
            when(builderMock.expiresAt(any(Instant.class))).thenReturn(builderMock);
            when(builderMock.sign()).thenReturn("TOK-NORMALIZADO");

            AuthRequestDTO request = AuthRequestDTO.builder()
                    .username(" ADMIN ")
                    .password("admin@123teste") // senha correta!
                    .build();

            AuthResponseDTO resp = authService.autenticar(request);

            assertEquals("TOK-NORMALIZADO", resp.getToken());
        }
    }
}

