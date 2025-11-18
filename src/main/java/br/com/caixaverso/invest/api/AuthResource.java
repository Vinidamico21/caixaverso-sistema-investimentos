package br.com.caixaverso.invest.api;

import br.com.caixaverso.invest.application.dto.AuthRequestDTO;
import br.com.caixaverso.invest.application.dto.AuthResponseDTO;
import br.com.caixaverso.invest.application.service.AuthService;
import br.com.caixaverso.invest.infra.exception.ApiErrorDTO;
import br.com.caixaverso.invest.infra.exception.BusinessException;
import br.com.caixaverso.invest.infra.exception.UnauthorizedException;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

import java.util.Set;

@RequestScoped
@Path("/api/v1/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Autenticação", description = "Endpoints para autenticação e emissão de token JWT")
public class AuthResource {

    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    private String rid() {
        return (String) MDC.get("requestId");
    }

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    @PermitAll
    @Operation(
            summary = "Realiza login e retorna token JWT",
            description = "Valida as credenciais (username/password) e emite um token JWT para acesso aos demais endpoints protegidos."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Autenticação realizada com sucesso.",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Requisição inválida (username/password ausentes).",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno ao processar a autenticação.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    public Response login(
            @RequestBody(
                    required = true,
                    description = "Credenciais de autenticação do usuário.",
                    content = @Content(schema = @Schema(implementation = AuthRequestDTO.class))
            )
            AuthRequestDTO request) {

        LOG.infof("[reqId=%s] Login iniciado", rid());

        if (request == null ||
                request.getUsername() == null ||
                request.getPassword() == null) {

            LOG.warnf("[reqId=%s] Requisicao invalida: username/password ausentes", rid());
            throw new BusinessException("username e password são obrigatórios");
        }

        String username = request.getUsername().trim().toLowerCase();
        String password = request.getPassword().trim().toLowerCase();

        LOG.debugf("[reqId=%s] Credenciais recebidas | username=%s", rid(), username);

        Set<String> roles;

        if ("admin".equals(username) && "admin".equals(password)) {
            roles = Set.of("ADMIN", "USER");
        } else if ("user".equals(username) && "user".equals(password)) {
            roles = Set.of("USER");
        } else {
            LOG.warnf("[reqId=%s] Credenciais inválidas | username=%s", rid(), username);
            throw new UnauthorizedException("Credenciais inválidas");
        }

        LOG.infof("[reqId=%s] Autenticacao bem-sucedida | username=%s | roles=%s",
                rid(), username, roles);

        String token = authService.gerarToken(username, roles);

        AuthResponseDTO response = AuthResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(authService.getTokenExpirationSeconds())
                .build();

        LOG.infof("[reqId=%s] Token gerado com sucesso | username=%s", rid(), username);

        return Response.ok(response).build();
    }
}
