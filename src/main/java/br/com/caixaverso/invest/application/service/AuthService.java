package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.AuthRequestDTO;
import br.com.caixaverso.invest.application.dto.AuthResponseDTO;
import br.com.caixaverso.invest.application.port.in.AuthUseCase;
import br.com.caixaverso.invest.infra.exception.BusinessException;
import br.com.caixaverso.invest.infra.exception.UnauthorizedException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Set;

@ApplicationScoped
public class AuthService implements AuthUseCase {

    @ConfigProperty(name = "jwt.expiration.seconds", defaultValue = "3600")
    public long expirationSeconds;

    @Override
    public AuthResponseDTO autenticar(AuthRequestDTO request) {
        validarRequest(request);

        String username = normalizar(request.getUsername());
        String password = normalizar(request.getPassword());

        Set<String> roles = autenticarUsuario(username, password);

        String token = gerarToken(username, roles);

        return AuthResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expirationSeconds)
                .build();
    }

    private void validarRequest(AuthRequestDTO request) {
        if (request == null ||
                request.getUsername() == null ||
                request.getPassword() == null) {
            throw new BusinessException("username e password são obrigatórios");
        }
    }

    private String normalizar(String str) {
        return str.trim().toLowerCase();
    }

    private Set<String> autenticarUsuario(String username, String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            return Set.of("ADMIN", "USER");
        }
        if ("user".equals(username) && "user".equals(password)) {
            return Set.of("USER");
        }

        throw new UnauthorizedException("Credenciais inválidas");
    }

    public String gerarToken(String username, Set<String> roles) {
        Instant agora = Instant.now();

        return Jwt.issuer("caixaverso-investimentos")
                .subject(username)
                .upn(username)
                .groups(roles)
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(expirationSeconds))
                .sign();
    }
}
