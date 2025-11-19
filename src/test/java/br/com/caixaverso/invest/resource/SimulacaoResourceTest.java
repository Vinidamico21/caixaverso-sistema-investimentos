package br.com.caixaverso.invest.resource;

import br.com.caixaverso.invest.application.dto.*;
import br.com.caixaverso.invest.application.port.in.AgruparSimulacoesPorProdutoDiaUseCase;
import br.com.caixaverso.invest.application.port.in.ListarSimulacoesUseCase;
import br.com.caixaverso.invest.application.port.in.SimularInvestimentoUseCase;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(SimulacaoResource.class)
class SimulacaoResourceTest {

    @InjectMock
    SimularInvestimentoUseCase simularUseCase;

    @InjectMock
    ListarSimulacoesUseCase listarUseCase;

    @InjectMock
    AgruparSimulacoesPorProdutoDiaUseCase agrupamentoUseCase;

    // ------------------------------------------------------
    // TESTE 1 — POST /simular-investimento (200 OK)
    // ------------------------------------------------------
    @Test
    @TestSecurity(user = "tester", roles = { "USER" })
    void testSimularInvestimentoSuccess() {

        SimularInvestimentoRequest request = new SimularInvestimentoRequest(
                1L,
                new BigDecimal("1000.00"),
                12,
                "CDB"
        );

        ProdutoValidadoDTO produto = new ProdutoValidadoDTO(
                10L, "CDB Premium", "CDB", new BigDecimal("0.12"), "MEDIO"
        );

        ResultadoSimulacaoDTO resultado = new ResultadoSimulacaoDTO(
                new BigDecimal("1120.00"),
                new BigDecimal("0.12"),
                12
        );

        SimularInvestimentoResponse mockResponse =
                new SimularInvestimentoResponse(produto, resultado, OffsetDateTime.now());

        when(simularUseCase.executarSimulacao(any())).thenReturn(mockResponse);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(request)
                .when()
                .post("/simular-investimento")
                .then()
                .statusCode(200)
                .body("produtoValidado.nome", is("CDB Premium"))
                .body("resultadoSimulacao.valorFinal", is(1120.00F))
                .body("resultadoSimulacao.prazoMeses", is(12));

        verify(simularUseCase, times(1)).executarSimulacao(any());
        verifyNoMoreInteractions(simularUseCase);
    }

    // ------------------------------------------------------
    // TESTE 2 — GET /simulacoes (sem filtro, com paginação default)
    // ------------------------------------------------------
    @Test
    @TestSecurity(user = "tester", roles = { "USER" })
    void testListarSimulacoesSemFiltro() {

        List<SimulacaoResumoDTO> mockList = List.of(
                new SimulacaoResumoDTO(
                        1L, 1L, "CDB Premium",
                        new BigDecimal("1000"),
                        new BigDecimal("1120"),
                        12,
                        OffsetDateTime.now()
                )
        );

        PageResponse<SimulacaoResumoDTO> mockPage =
                PageResponse.<SimulacaoResumoDTO>builder()
                        .content(mockList)
                        .page(0)
                        .size(20)
                        .totalElements(1L)
                        .totalPages(1)
                        .build();

        // assume defaults page=0, size=20 no resource
        when(listarUseCase.listarSimulacoes(null, 0, 20)).thenReturn(mockPage);

        given()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/simulacoes")
                .then()
                .statusCode(200)
                .body("content", hasSize(1))
                .body("content[0].produto", is("CDB Premium"))
                .body("page", is(0))
                .body("size", is(20))
                .body("totalElements", is(1))
                .body("totalPages", is(1));

        verify(listarUseCase, times(1)).listarSimulacoes(null, 0, 20);
        verifyNoMoreInteractions(listarUseCase);
    }

    // ------------------------------------------------------
    // TESTE 3 — GET /simulacoes (filtro válido + paginação explícita)
    // ------------------------------------------------------
    @Test
    @TestSecurity(user = "tester", roles = { "USER" })
    void testListarSimulacoesComFiltroValido() {

        List<SimulacaoResumoDTO> mockList = List.of(
                new SimulacaoResumoDTO(
                        2L, 123L, "Tesouro Selic",
                        new BigDecimal("1500"),
                        new BigDecimal("1600"),
                        6,
                        OffsetDateTime.now()
                )
        );

        PageResponse<SimulacaoResumoDTO> mockPage =
                PageResponse.<SimulacaoResumoDTO>builder()
                        .content(mockList)
                        .page(1)
                        .size(5)
                        .totalElements(1L)
                        .totalPages(1)
                        .build();

        when(listarUseCase.listarSimulacoes(123L, 1, 5)).thenReturn(mockPage);

        given()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/simulacoes?clienteId=123&page=1&size=5")
                .then()
                .statusCode(200)
                .body("content", hasSize(1))
                .body("content[0].clienteId", is(123))
                .body("page", is(1))
                .body("size", is(5))
                .body("totalElements", is(1))
                .body("totalPages", is(1));

        verify(listarUseCase, times(1)).listarSimulacoes(123L, 1, 5);
        verifyNoMoreInteractions(listarUseCase);
    }

    // ------------------------------------------------------
    // TESTE 4 — GET /simulacoes (clienteId inválido → BusinessException)
    // ------------------------------------------------------
    @Test
    @TestSecurity(user = "tester", roles = { "USER" })
    void testListarSimulacoesClienteIdInvalido() {

        given()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/simulacoes?clienteId=abc")
                .then()
                .statusCode(400)
                .body("message", containsStringIgnoringCase("Parâmetro 'clienteId' deve ser um inteiro válido."));

        verify(listarUseCase, never()).listarSimulacoes(any(), anyInt(), anyInt());
        verifyNoMoreInteractions(listarUseCase);
    }

    // ------------------------------------------------------
    // TESTE 5 — GET /simulacoes/por-produto-dia (com paginação)
    // ------------------------------------------------------
    @Test
    @TestSecurity(user = "tester", roles = { "USER" })
    void testListarSimulacoesPorProdutoDiaSuccess() {

        List<SimulacaoPorProdutoDiaDTO> mockList = List.of(
                new SimulacaoPorProdutoDiaDTO(
                        "CDB Premium",
                        LocalDate.of(2024, 1, 10),
                        5L,
                        new BigDecimal("1100.50")
                )
        );

        PageResponse<SimulacaoPorProdutoDiaDTO> mockPage =
                PageResponse.<SimulacaoPorProdutoDiaDTO>builder()
                        .content(mockList)
                        .page(0)
                        .size(10)
                        .totalElements(1L)
                        .totalPages(1)
                        .build();

        when(agrupamentoUseCase.agrupamentoPorProdutoDia(0, 10)).thenReturn(mockPage);

        given()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/simulacoes/por-produto-dia?page=0&size=10")
                .then()
                .statusCode(200)
                .body("content", hasSize(1))
                .body("content[0].produto", is("CDB Premium"))
                .body("content[0].quantidadeSimulacoes", is(5))
                .body("page", is(0))
                .body("size", is(10))
                .body("totalElements", is(1))
                .body("totalPages", is(1));

        verify(agrupamentoUseCase, times(1)).agrupamentoPorProdutoDia(0, 10);
        verifyNoMoreInteractions(agrupamentoUseCase);
    }
}
