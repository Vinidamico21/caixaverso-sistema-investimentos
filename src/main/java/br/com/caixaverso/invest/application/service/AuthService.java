package br.com.caixaverso.invest.application.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @ConfigProperty(name = "jwt.expiration.seconds", defaultValue = "3600")
    long expirationSeconds;

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

    public long getTokenExpirationSeconds() {
        return expirationSeconds;
    }
}
