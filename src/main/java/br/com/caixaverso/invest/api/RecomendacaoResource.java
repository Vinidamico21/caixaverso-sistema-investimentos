package br.com.caixaverso.invest.api;

import br.com.caixaverso.invest.application.dto.ProdutoRecomendadoDTO;
import br.com.caixaverso.invest.application.dto.RecomendacaoResponseDTO;
import br.com.caixaverso.invest.application.service.MotorRecomendacaoService;
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
    MotorRecomendacaoService motor;

    @GET
    @Path("/clientes/{clienteId}/recomendacoes")
    @Operation(
            summary = "Recomenda produtos para um cliente",
            description = "Utiliza o motor de recomendação para calcular o perfil de risco do cliente "
                    + "e retornar a lista de produtos mais aderentes a esse perfil. "
                    + "Caso o cliente não possua histórico suficiente, o perfil é marcado como DESCONHECIDO e a lista vem vazia."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Recomendações geradas com sucesso.",
                    content = @Content(schema = @Schema(implementation = RecomendacaoResponseDTO.class))
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
                    description = "Erro interno ao processar as recomendações.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    public Response recomendarParaCliente(
            @Parameter(
                    description = "Identificador numérico do cliente.",
                    required = true,
                    example = "123"
            )
            @PathParam("clienteId") String clienteIdRaw) {

        Long clienteId = parseClienteIdToLong(clienteIdRaw);

        RecomendacaoResponseDTO response = motor.recomendarParaCliente(clienteId);
        return Response.ok(response).build();
    }

    @GET
    @Path("/produtos-recomendados/{perfil}")
    @Operation(
            summary = "Lista produtos recomendados por perfil de risco",
            description = "Retorna a lista de produtos indicados para um perfil de risco informado "
                    + "(por exemplo: CONSERVADOR, MODERADO, AGRESSIVO). "
                    + "Caso o perfil não seja informado ou seja inválido, é utilizado um perfil padrão (conservador)."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Lista de produtos recomendados retornada com sucesso.",
                    content = @Content(schema = @Schema(implementation = ProdutoRecomendadoDTO.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Requisição não autorizada (token ausente ou inválido).",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno ao processar as recomendações por perfil.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    public Response listarPorPerfil(
            @Parameter(
                    description = "Perfil de risco textual (CONSERVADOR, MODERADO, AGRESSIVO).",
                    required = true,
                    example = "MODERADO"
            )
            @PathParam("perfil") String perfil) {

        List<ProdutoRecomendadoDTO> produtos = motor.recomendarPorPerfil(perfil);
        return Response.ok(produtos).build();
    }
}
