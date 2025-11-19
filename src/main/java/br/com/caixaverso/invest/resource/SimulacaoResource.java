package br.com.caixaverso.invest.resource;

import br.com.caixaverso.invest.application.dto.*;
import br.com.caixaverso.invest.application.dto.request.SimularInvestimentoRequest;
import br.com.caixaverso.invest.application.dto.response.PageResponse;
import br.com.caixaverso.invest.application.dto.response.SimularInvestimentoResponse;
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
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = SimularInvestimentoResponse.class)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Dados inválidos.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    public Response simularInvestimento(
            @RequestBody(required = true,
                    description = "Dados para simulação de investimento")
            @Valid SimularInvestimentoRequest request) {

        SimularInvestimentoResponse response =
                simularUseCase.executarSimulacao(request);

        return Response.ok(response).build();
    }

    @GET
    @Path("/simulacoes")
    @Operation(
            summary = "Lista simulações realizadas",
            description = "Retorna o histórico de simulações realizadas com paginação em memória, " +
                    "opcionalmente filtrando por cliente."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Página de simulações retornada com sucesso.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(
                                    implementation = PageResponse.class,
                                    description = "Página contendo SimulacaoResumoDTO"
                            )
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Parâmetro 'clienteId' inválido ou parâmetros de paginação inválidos.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    public Response listarSimulacoes(
            @Parameter(
                    description = "Filtro opcional pelo ID do cliente",
                    example = "10"
            )
            @QueryParam("clienteId") String clienteIdParam,
            @Parameter(
                    description = "Número da página (0-based)",
                    example = "0"
            )
            @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(
                    description = "Quantidade de registros por página",
                    example = "20"
            )
            @QueryParam("size") @DefaultValue("20") int size) {

        Long clienteId = null;

        if (clienteIdParam != null && !clienteIdParam.isBlank()) {
            try {
                clienteId = Long.parseLong(clienteIdParam);
            } catch (NumberFormatException e) {
                throw new BusinessException("Parâmetro 'clienteId' deve ser um inteiro válido.");
            }
        }

        PageResponse<SimulacaoResumoDTO> pagina =
                listarUseCase.listarSimulacoes(clienteId, page, size);

        return Response.ok(pagina).build();
    }

    @GET
    @Path("/simulacoes/por-produto-dia")
    @Operation(
            summary = "Consolida simulações por produto e dia",
            description = "Retorna estatísticas agregadas de simulações por produto e data (quantidade e média do valor final), com paginação em memória."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Consulta de simulações por produto/dia realizada com sucesso.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(
                                    implementation = PageResponse.class,
                                    description = "Página de resultados contendo SimulacaoPorProdutoDiaDTO"
                            )
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Parâmetros de paginação inválidos.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    public Response listarSimulacoesPorProdutoDia(
            @Parameter(
                    description = "Número da página (0-based)",
                    example = "0"
            )
            @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(
                    description = "Quantidade de registros por página",
                    example = "20"
            )
            @QueryParam("size") @DefaultValue("20") int size) {

        PageResponse<SimulacaoPorProdutoDiaDTO> resposta =
                agrupamentoUseCase.agrupamentoPorProdutoDia(page, size);

        return Response.ok(resposta).build();
    }
}