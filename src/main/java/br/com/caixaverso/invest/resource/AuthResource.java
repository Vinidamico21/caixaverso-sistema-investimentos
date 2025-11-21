package br.com.caixaverso.invest.resource;

import br.com.caixaverso.invest.application.dto.AuthRequestDTO;
import br.com.caixaverso.invest.application.dto.AuthResponseDTO;
import br.com.caixaverso.invest.application.port.in.AuthUseCase;
import br.com.caixaverso.invest.infra.exception.ApiErrorDTO;
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

@RequestScoped
@Path("/api/v1/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Autenticação", description = "Endpoints para autenticação e emissão de token JWT")
public class AuthResource {

    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    @Inject
    AuthUseCase authUseCase;

    @POST
    @Path("/login")
    @PermitAll
    @Operation(
            summary = "Realiza login e retorna token JWT",
            description = "Valida credenciais e emite um token JWT para uso nos endpoints autenticados. Consulte o README do projeto para visualizar as credenciais de exemplo na seção '4.0'."
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
                    description = "Credenciais do usuário para autenticação.",
                    content = @Content(schema = @Schema(implementation = AuthRequestDTO.class))
            )
            AuthRequestDTO request) {

        LOG.info("Login iniciado");

        AuthResponseDTO response = authUseCase.autenticar(request);

        LOG.info("Login concluído com sucesso");

        return Response.ok(response).build();
    }
}
