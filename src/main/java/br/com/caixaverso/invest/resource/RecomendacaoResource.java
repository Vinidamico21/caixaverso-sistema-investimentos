package br.com.caixaverso.invest.resource;

import br.com.caixaverso.invest.application.dto.ProdutoRecomendadoDTO;
import br.com.caixaverso.invest.application.dto.RecomendacaoResponseDTO;
import br.com.caixaverso.invest.application.port.in.RecomendarProdutosUseCase;
import br.com.caixaverso.invest.domain.enums.PerfilRisco;
import br.com.caixaverso.invest.infra.exception.ApiErrorDTO;
import br.com.caixaverso.invest.infra.exception.BusinessException;
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

import java.util.List;

import static br.com.caixaverso.invest.infra.util.ClienteIdParser.parseClienteIdToLong;

@SecurityRequirement(name = "Authorization")
@RequestScoped
@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("USER")
@Tag(name = "Recomendações", description = "Motor de recomendação de produtos de investimento por cliente ou perfil de risco")
public class RecomendacaoResource {

    @Inject
    RecomendarProdutosUseCase recomendacaoUseCase;

    @GET
    @Path("/clientes/{clienteId}/recomendacoes")
    @Operation(
            summary = "Recomenda produtos para um cliente",
            description = "Calcula o perfil do cliente e retorna produtos recomendados baseados no score final."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Recomendações geradas com sucesso.",
                    content = @Content(schema = @Schema(implementation = RecomendacaoResponseDTO.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Parâmetro clienteId inválido",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno ao gerar recomendações",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    public Response recomendarParaCliente(
            @Parameter(description = "ID do cliente", required = true)
            @PathParam("clienteId") String clienteIdRaw) {

        Long clienteId = parseClienteIdToLong(clienteIdRaw);

        RecomendacaoResponseDTO response = recomendacaoUseCase.recomendarParaCliente(clienteId);

        return Response.ok(response).build();
    }

    @GET
    @Path("/produtos-recomendados/{perfil}")
    @Operation(
            summary = "Recomenda produtos com base em um perfil textual",
            description = "Lista de produtos recomendados para um perfil de risco informado."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Produtos recomendados retornados com sucesso.",
                    content = @Content(schema = @Schema(implementation = ProdutoRecomendadoDTO.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno ao gerar recomendações",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    public Response listarPorPerfil(@PathParam("perfil") String perfil) {

        PerfilRisco perfilRisco = PerfilRisco.from(perfil);

        if (perfilRisco == PerfilRisco.DESCONHECIDO) {
            throw new BusinessException(
                    "Parâmetro 'perfil' inválido. Valores permitidos: CONSERVADOR, MODERADO, AGRESSIVO."
            );
        }
        List<ProdutoRecomendadoDTO> produtos =
                recomendacaoUseCase.recomendarPorPerfil(perfilRisco.name());

        return Response.ok(produtos).build();
    }
}