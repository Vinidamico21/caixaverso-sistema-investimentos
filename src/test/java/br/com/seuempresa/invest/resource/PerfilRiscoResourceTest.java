package br.com.seuempresa.invest.resource;

import br.com.caixaverso.invest.application.dto.PerfilRiscoResponseDTO;
import br.com.caixaverso.invest.application.port.in.CalcularPerfilRiscoUseCase;
import br.com.caixaverso.invest.resource.PerfilRiscoResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(PerfilRiscoResource.class)
class PerfilRiscoResourceTest {

    @InjectMock
    CalcularPerfilRiscoUseCase calcularPerfilRiscoUseCase;

    @Test
    @TestSecurity(user = "tester", roles = { "USER" })
    void testObterPerfilSuccess() {
        PerfilRiscoResponseDTO mockDto = new PerfilRiscoResponseDTO(
                123L,
                "MODERADO",
                "0.55",
                "Perfil moderado com tendência equilibrada"
        );

        when(calcularPerfilRiscoUseCase.calcularPerfil(123L)).thenReturn(mockDto);

        given()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/123") // base path vem de @TestHTTPEndpoint
                .then()
                .statusCode(200)
                .body("perfil", is("MODERADO"))
                .body("pontuacao", is("0.55"));

        verify(calcularPerfilRiscoUseCase, times(1)).calcularPerfil(123L);
        verifyNoMoreInteractions(calcularPerfilRiscoUseCase);
    }

    @Test
    @TestSecurity(user = "tester", roles = { "USER" })
    void testObterPerfilNotFound() {
        when(calcularPerfilRiscoUseCase.calcularPerfil(999L)).thenReturn(null);

        given()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/999")
                .then()
                .statusCode(404)
                // se seu ExceptionMapper usa "mensagem", este assert passa;
                // se usar "message", troque abaixo para "message".
                .body("message", containsStringIgnoringCase("não encontrado"));

        verify(calcularPerfilRiscoUseCase, times(1)).calcularPerfil(999L);
        verifyNoMoreInteractions(calcularPerfilRiscoUseCase);
    }
}
