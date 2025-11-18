package br.com.caixaverso.invest.api;

import br.com.caixaverso.invest.application.dto.TelemetriaResponseDTO;
import br.com.caixaverso.invest.application.service.TelemetriaService;
import br.com.caixaverso.invest.infra.exception.ApiErrorDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@SecurityRequirement(name = "Authorization")
@RequestScoped
@Path("/api/v1/telemetria")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
@Tag(name = "Telemetria", description = "Relatórios de uso da API (apenas para administradores)")
public class TelemetriaResource {

    @Inject
    TelemetriaService telemetriaService;

    @GET
    @Operation(
            summary = "Obtém relatório de telemetria da API",
            description = "Retorna métricas agregadas de uso da API, incluindo quantidade de chamadas e "
                    + "tempo médio de resposta por endpoint, além do período coberto pelo relatório."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Relatório de telemetria gerado com sucesso.",
                    content = @Content(schema = @Schema(implementation = TelemetriaResponseDTO.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Requisição não autorizada (token ausente ou inválido).",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Acesso negado. Usuário não possui o papel ADMIN.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno ao gerar o relatório de telemetria.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    public Response obterTelemetria() {
        TelemetriaResponseDTO dto = telemetriaService.gerarRelatorio();
        return Response.ok(dto).build();
    }
}
