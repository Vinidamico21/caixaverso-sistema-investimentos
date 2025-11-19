package br.com.seuempresa.invest.resource;

import br.com.caixaverso.invest.application.dto.InvestimentoResponseDTO;
import br.com.caixaverso.invest.application.port.in.ListarInvestimentosPorClienteUseCase;
import br.com.caixaverso.invest.resource.InvestimentoResource;
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
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(InvestimentoResource.class)
class InvestimentoResourceTest {

    @InjectMock
    ListarInvestimentosPorClienteUseCase listarInvestimentosUseCase;

    @Test
    @TestSecurity(user = "tester", roles = { "USER" }) // simula autenticação/autorizaçao
    void testListarPorClienteSuccess() {

        List<InvestimentoResponseDTO> mockLista = List.of(
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

        when(listarInvestimentosUseCase.listarPorCliente("123"))
                .thenReturn(mockLista);

        given()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/123") // com @TestHTTPEndpoint, o path do recurso é resolvido
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].produto", is("CDB Premium"))
                .body("[0].valorAplicado", is(1500.00F)) // número como float p/ evitar escala
                .body("[0].rentabilidade", is(0.12F))
                .body("[0].dataAplicacao", is("2024-01-10"))
                .body("[1].produto", is("Tesouro Selic"));

        verify(listarInvestimentosUseCase, times(1))
                .listarPorCliente("123");
        verifyNoMoreInteractions(listarInvestimentosUseCase);
    }
}
