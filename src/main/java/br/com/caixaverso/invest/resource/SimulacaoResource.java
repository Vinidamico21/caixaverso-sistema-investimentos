package br.com.caixaverso.invest.resource;

import br.com.caixaverso.invest.application.dto.*;
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
import br.com.caixaverso.invest.application.port.in.AgruparSimulacoesPorProdutoDiaUseCase;
import br.com.caixaverso.invest.application.port.in.ListarSimulacoesUseCase;
import br.com.caixaverso.invest.application.port.in.SimularInvestimentoUseCase;

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
    SimularInvestimentoUseCase simularUseCase;

    @Inject
    ListarSimulacoesUseCase listarUseCase;

    @Inject
    AgruparSimulacoesPorProdutoDiaUseCase agrupamentoUseCase;

    // ----------------------------------------------------
    // POST /api/v1/simular-investimento
    // ----------------------------------------------------
    @POST
    @Path("/simular-investimento")
    @Operation(
            summary = "Simula um investimento",
            description = "Executa a simulação de um investimento conforme os dados informados."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Simulação realizada com sucesso.",
                    content = @Content(schema = @Schema(implementation = SimularInvestimentoResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Dados inválidos.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    public Response simularInvestimento(
            @RequestBody(required = true)
            @Valid SimularInvestimentoRequest request) {

        SimularInvestimentoResponse response =
                simularUseCase.executarSimulacao(request);

        return Response.ok(response).build();
    }

    // ----------------------------------------------------
    // GET /api/v1/simulacoes?clienteId=...
    // ----------------------------------------------------
    @GET
    @Path("/simulacoes")
    @Operation(summary = "Lista simulações realizadas")
    public Response listarSimulacoes(
            @Parameter(description = "Filtro opcional pelo ID do cliente")
            @QueryParam("clienteId") String clienteIdParam) {

        Long clienteId = null;

        if (clienteIdParam != null && !clienteIdParam.isBlank()) {
            try {
                clienteId = Long.parseLong(clienteIdParam);
            } catch (NumberFormatException e) {
                throw new BusinessException("Parâmetro 'clienteId' deve ser um inteiro válido.");
            }
        }

        List<SimulacaoResumoDTO> lista = listarUseCase.listarSimulacoes(clienteId);
        return Response.ok(lista).build();
    }

    // ----------------------------------------------------
    // GET /api/v1/simulacoes/por-produto-dia
    // ----------------------------------------------------
    @GET
    @Path("/simulacoes/por-produto-dia")
    @Operation(summary = "Consolida simulações por produto e dia")
    public Response listarSimulacoesPorProdutoDia() {

        List<SimulacaoPorProdutoDiaDTO> resposta =
                agrupamentoUseCase.agrupamentoPorProdutoDia();

        return Response.ok(resposta).build();
    }
}
