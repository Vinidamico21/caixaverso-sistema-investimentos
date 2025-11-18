package br.com.caixaverso.invest.api;

import br.com.caixaverso.invest.application.dto.PerfilRiscoResponseDTO;
import br.com.caixaverso.invest.application.service.MotorRecomendacaoService;
import br.com.caixaverso.invest.infra.exception.ApiErrorDTO;
import br.com.caixaverso.invest.infra.exception.NotFoundException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static br.com.caixaverso.invest.infra.util.ClienteIdParser.parseClienteIdToLong;

@SecurityRequirement(name = "Authorization")
@RequestScoped
@Path("/api/v1/perfil-risco")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("USER")
@Tag(name = "Perfil de Risco", description = "Cálculo dinâmico de perfil de risco do cliente com base no histórico de investimentos e simulações")
public class PerfilRiscoResource {

    @Inject
    MotorRecomendacaoService motor;

    @GET
    @Path("/{clienteId}")
    @Operation(
            summary = "Obtém o perfil de risco do cliente",
            description = "Calcula o perfil de risco do cliente com base nas simulações e investimentos realizados, "
                    + "considerando volume, frequência e preferência por liquidez ou rentabilidade."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Perfil de risco calculado com sucesso.",
                    content = @Content(schema = @Schema(implementation = PerfilRiscoResponseDTO.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Parâmetro 'clienteId' inválido (nulo, vazio ou não numérico).",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Requisição não autorizada (token ausente ou inválido).",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Nenuma simulação encontrada para o cliente ou perfil de risco inexistente.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno ao calcular o perfil de risco.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    public Response obterPerfil(
            @Parameter(
                    description = "Identificador numérico do cliente.",
                    required = true,
                    example = "123"
            )
            @PathParam("clienteId") String clienteIdRaw) {

        Long clienteId = parseClienteIdToLong(clienteIdRaw);

        PerfilRiscoResponseDTO dto = motor.calcularPerfil(clienteId);

        if (dto == null) {
            throw new NotFoundException("Perfil de risco não encontrado para clienteId=" + clienteId);
        }

        return Response.ok(dto).build();
    }
}
