package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.AuthRequestDTO;
import br.com.caixaverso.invest.application.dto.AuthResponseDTO;
import br.com.caixaverso.invest.application.port.in.AuthUseCase;
import br.com.caixaverso.invest.domain.constants.PerfilConstantes;
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
                .tokenType(PerfilConstantes.AUTH_JWT_TOKEN_TYPE)
                .expiresIn(expirationSeconds)
                .build();
    }

    private void validarRequest(AuthRequestDTO request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            throw new BusinessException(PerfilConstantes.AUTH_ERRO_CREDENCIAIS_OBRIGATORIAS);
        }
    }

    private String normalizar(String str) {
        return str.trim().toLowerCase();
    }

    private Set<String> autenticarUsuario(String username, String password) {

        if (PerfilConstantes.AUTH_USER_ADMIN.equals(username)
                && PerfilConstantes.AUTH_USER_ADMIN.equals(password)) {

            return Set.of(
                    PerfilConstantes.AUTH_ROLE_ADMIN,
                    PerfilConstantes.AUTH_ROLE_USER
            );
        }

        if (PerfilConstantes.AUTH_USER_PADRAO.equals(username)
                && PerfilConstantes.AUTH_USER_PADRAO.equals(password)) {

            return Set.of(PerfilConstantes.AUTH_ROLE_USER);
        }

        throw new UnauthorizedException(PerfilConstantes.AUTH_ERRO_CREDENCIAIS_INVALIDAS);
    }

    public String gerarToken(String username, Set<String> roles) {

        Instant agora = Instant.now();

        return Jwt.issuer(PerfilConstantes.AUTH_JWT_ISSUER)
                .subject(username)
                .upn(username)
                .groups(roles)
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(expirationSeconds))
                .sign();
    }
}
