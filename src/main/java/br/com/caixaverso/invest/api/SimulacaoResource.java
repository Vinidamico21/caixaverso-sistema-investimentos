package br.com.caixaverso.invest.api;

import br.com.caixaverso.invest.application.dto.*;
import br.com.caixaverso.invest.application.service.SimulacaoService;
import br.com.caixaverso.invest.infra.exception.ApiErrorDTO;
import br.com.caixaverso.invest.infra.exception.BusinessException;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@SecurityRequirement(name = "Authorization")
@RequestScoped
@Path("/api/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("USER")
@Tag(name = "Simulações", description = "Endpoints para simulação de investimentos e consulta de histórico de simulações")
public class SimulacaoResource {

    @Inject
    SimulacaoService simulacaoService;

    // ----------------------------------------------------
    // POST /api/v1/simular-investimento
    // ----------------------------------------------------
    @POST
    @Path("/simular-investimento")
    @Operation(
            summary = "Simula um investimento",
            description = "Recebe os dados de uma solicitação de simulação (cliente, valor, prazo e tipo de produto), "
                    + "seleciona o melhor produto disponível e retorna o resultado da simulação com valor final e rentabilidade."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Simulação realizada com sucesso.",
                    content = @Content(schema = @Schema(implementation = SimularInvestimentoResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Requisição inválida (erros de validação nos campos de entrada).",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Requisição não autorizada (token ausente ou inválido).",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "422",
                    description = "Regra de negócio violada (por exemplo, nenhum produto encontrado para o tipo informado).",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno ao processar a simulação.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    public Response simularInvestimento(
            @RequestBody(
                    required = true,
                    description = "Dados da solicitação de simulação de investimento.",
                    content = @Content(schema = @Schema(implementation = SimularInvestimentoRequest.class))
            )
            @Valid SimularInvestimentoRequest request) {

        SimularInvestimentoResponse response =
                simulacaoService.executarSimulacao(request);

        return Response.ok(response).build();
    }

    // ----------------------------------------------------
    // GET /api/v1/simulacoes?clienteId=...
    // ----------------------------------------------------
    @GET
    @Path("/simulacoes")
    @Operation(
            summary = "Lista simulações realizadas",
            description = "Retorna o histórico de simulações realizadas. "
                    + "Caso o parâmetro 'clienteId' seja informado, filtra as simulações apenas para o cliente especificado."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Lista de simulações retornada com sucesso.",
                    content = @Content(schema = @Schema(implementation = SimulacaoResumoDTO.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Parâmetro 'clienteId' inválido (não numérico).",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Requisição não autorizada (token ausente ou inválido).",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno ao consultar as simulações.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    public Response listarSimulacoes(
            @Parameter(
                    description = "Identificador numérico do cliente para filtro opcional. "
                            + "Se não informado, retorna as simulações de todos os clientes.",
                    required = false,
                    example = "123"
            )
            @QueryParam("clienteId") String clienteIdParam) {

        Long clienteId = null;

        if (clienteIdParam != null && !clienteIdParam.isBlank()) {
            try {
                clienteId = Long.parseLong(clienteIdParam);
            } catch (NumberFormatException e) {
                throw new BusinessException("Parâmetro 'clienteId' deve ser um número inteiro.");
            }
        }

        List<SimulacaoResumoDTO> lista = simulacaoService.listarSimulacoes(clienteId);

        return Response.ok(lista).build();
    }

    // ----------------------------------------------------
    // GET /api/v1/simulacoes/por-produto-dia
    // ----------------------------------------------------
    @GET
    @Path("/simulacoes/por-produto-dia")
    @Operation(
            summary = "Consolida simulações por produto e dia",
            description = "Retorna um resumo estatístico das simulações realizadas, agrupando por produto e dia. "
                    + "Para cada combinação produto/data, informa a quantidade de simulações e a média do valor final."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Resumo de simulações por produto e dia retornado com sucesso.",
                    content = @Content(schema = @Schema(implementation = SimulacaoPorProdutoDiaDTO.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Requisição não autorizada (token ausente ou inválido).",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno ao gerar o agrupamento de simulações.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    public Response listarSimulacoesPorProdutoDia() {
        List<SimulacaoPorProdutoDiaDTO> resposta =
                simulacaoService.agrupamentoPorProdutoDia();
        return Response.ok(resposta).build();
    }
}
