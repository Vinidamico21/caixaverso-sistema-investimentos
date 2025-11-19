package br.com.caixaverso.invest.resource;

import br.com.caixaverso.invest.application.dto.ProdutoRecomendadoDTO;
import br.com.caixaverso.invest.application.dto.RecomendacaoResponseDTO;
import br.com.caixaverso.invest.application.port.in.RecomendarProdutosUseCase;
import br.com.caixaverso.invest.resource.RecomendacaoResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(RecomendacaoResource.class)
class RecomendacaoResourceTest {

    @InjectMock
    RecomendarProdutosUseCase recomendacaoUseCase;

    @Test
    @TestSecurity(user = "tester", roles = { "USER" })
    void testRecomendarParaClienteSuccess() {
        RecomendacaoResponseDTO mockResponse = new RecomendacaoResponseDTO(
                123L,
                "MODERADO",
                "0.63",
                List.of(
                        new ProdutoRecomendadoDTO(
                                1L, "CDB Premium", "CDB", new BigDecimal("0.12"), "MEDIO"
                        ),
                        new ProdutoRecomendadoDTO(
                                2L, "Tesouro Selic", "TESOURO", new BigDecimal("0.10"), "BAIXO"
                        )
                )
        );

        when(recomendacaoUseCase.recomendarParaCliente(123L)).thenReturn(mockResponse);

        given()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/clientes/123/recomendacoes") // base path vem do @TestHTTPEndpoint
                .then()
                .statusCode(200)
                .body("clienteId", is(123))
                .body("perfilRisco", is("MODERADO"))
                .body("scoreRisco", is("0.63"))
                .body("produtos", hasSize(2))
                .body("produtos[0].nome", is("CDB Premium"))
                .body("produtos[0].tipo", is("CDB"))
                .body("produtos[0].rentabilidade", is(0.12F))
                .body("produtos[0].risco", is("MEDIO"));

        verify(recomendacaoUseCase, times(1)).recomendarParaCliente(123L);
        verifyNoMoreInteractions(recomendacaoUseCase);
    }

    @Test
    @TestSecurity(user = "tester", roles = { "USER" })
    void testRecomendarParaClienteInternalError() {
        when(recomendacaoUseCase.recomendarParaCliente(999L))
                .thenThrow(new RuntimeException("erro interno"));

        given()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/clientes/999/recomendacoes")
                .then()
                .statusCode(500)
                // troque "mensagem" por "message" se seu ExceptionMapper usar inglês
                .body("message", containsStringIgnoringCase("Erro inesperado ao processar a requisição"));
    verify(recomendacaoUseCase, times(1)).recomendarParaCliente(999L);
        verifyNoMoreInteractions(recomendacaoUseCase);
    }
}
