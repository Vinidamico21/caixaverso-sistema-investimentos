package br.com.caixaverso.invest.resource;

import br.com.caixaverso.invest.application.dto.InvestimentoResponseDTO;
import br.com.caixaverso.invest.application.dto.response.PageResponse;
import br.com.caixaverso.invest.application.port.in.ListarInvestimentosPorClienteUseCase;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(InvestimentoResource.class)
class InvestimentoResourceTest {

    @InjectMock
    ListarInvestimentosPorClienteUseCase listarInvestimentosUseCase;

    @Test
    @TestSecurity(user = "tester", roles = { "USER" })
    void testListarPorClienteComPaginacaoSuccess() {

        List<InvestimentoResponseDTO> content = List.of(
                new InvestimentoResponseDTO(
                        1L,
                        "CDB Premium",
                        new BigDecimal("1500.00"),
                        new BigDecimal("0.12"),
                        LocalDate.of(2024, 1, 10)
                ),
                new InvestimentoResponseDTO(
                        2L,
                        "Tesouro Selic",
                        new BigDecimal("2000.00"),
                        new BigDecimal("0.10"),
                        LocalDate.of(2024, 2, 1)
                )
        );

        PageResponse<InvestimentoResponseDTO> mockPage =
                PageResponse.<InvestimentoResponseDTO>builder()
                        .content(content)
                        .page(0)
                        .size(10)
                        .totalElements(2L)
                        .totalPages(1)
                        .build();

        when(listarInvestimentosUseCase.listarPorCliente("123", 0, 10))
                .thenReturn(mockPage);

        given()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/123?page=0&size=10")
                .then()
                .statusCode(200)
                .body("content", hasSize(2))
                // campos conforme o DTO atual
                .body("content[0].produto", is("CDB Premium"))
                .body("content[0].valorAplicado", is(1500.00F))
                .body("content[0].rentabilidade", is(0.12F))
                .body("content[0].dataAplicacao", is("2024-01-10"))
                .body("content[1].produto", is("Tesouro Selic"))
                // metadados da página
                .body("page", is(0))
                .body("size", is(10))
                .body("totalElements", is(2))
                .body("totalPages", is(1));

        verify(listarInvestimentosUseCase, times(1))
                .listarPorCliente("123", 0, 10);
        verifyNoMoreInteractions(listarInvestimentosUseCase);
    }
}
