package br.com.caixaverso.invest.resource;

import br.com.caixaverso.invest.application.dto.InvestimentoResponseDTO;
import br.com.caixaverso.invest.application.dto.PageResponse;
import br.com.caixaverso.invest.application.port.in.ListarInvestimentosPorClienteUseCase;
import br.com.caixaverso.invest.infra.exception.ApiErrorDTO;
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

@SecurityRequirement(name = "Authorization")
@RequestScoped
@Path("/api/v1/investimentos")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("USER")
@Tag(name = "Investimentos", description = "Consulta de investimentos realizados pelos clientes")
public class InvestimentoResource {

    @Inject
    ListarInvestimentosPorClienteUseCase listarInvestimentosUseCase;

    @GET
    @Path("/{clienteId}")
    @Operation(
            summary = "Lista investimentos de um cliente",
            description = "Retorna a lista paginada de investimentos realizados por um cliente, " +
                    "ordenados da data mais recente para a mais antiga."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Página de investimentos retornada com sucesso.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(
                                    implementation = PageResponse.class,
                                    description = "Página de resultados contendo InvestimentoResponseDTO"
                            )
                    )
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
                    responseCode = "500",
                    description = "Erro interno ao buscar investimentos.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    public Response listarPorCliente(
            @Parameter(
                    description = "Identificador numérico do cliente.",
                    required = true,
                    example = "123"
            )
            @PathParam("clienteId") String clienteIdRaw,
            @Parameter(
                    description = "Número da página (0-based).",
                    example = "0"
            )
            @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(
                    description = "Quantidade de registros por página.",
                    example = "20"
            )
            @QueryParam("size") @DefaultValue("20") int size) {

        PageResponse<InvestimentoResponseDTO> resposta =
                listarInvestimentosUseCase.listarPorCliente(clienteIdRaw, page, size);

        return Response.ok(resposta).build();
    }
}
