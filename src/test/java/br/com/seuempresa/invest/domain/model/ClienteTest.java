package br.com.caixaverso.invest.resource;

import br.com.caixaverso.invest.application.dto.TelemetriaPeriodoDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaResponseDTO;
import br.com.caixaverso.invest.application.dto.TelemetriaServicoDTO;
import br.com.caixaverso.invest.application.port.in.GerarRelatorioTelemetriaUseCase;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class TelemetriaResourceTest {

    @InjectMock
    GerarRelatorioTelemetriaUseCase gerarRelatorioUseCase;

    private TelemetriaResponseDTO expectedDto;

    @BeforeEach
    void setUp() {
        TelemetriaServicoDTO servico = TelemetriaServicoDTO.builder()
                .nome("Servico Teste")
                .quantidadeChamadas(150L)
                .mediaTempoRespostaMs(85L)
                .build();

        TelemetriaPeriodoDTO periodo = TelemetriaPeriodoDTO.builder()
                .inicio(LocalDate.of(2023, 1, 1))
                .fim(LocalDate.of(2023, 1, 31))
                .build();

        expectedDto = TelemetriaResponseDTO.builder()
                .servicos(List.of(servico))
                .periodo(periodo)
                .build();
    }

    @Test
    @TestSecurity(user = "testuser", roles = "ADMIN")
    void testObterTelemetria_ComSucesso_Retorna200() {
        Mockito.when(gerarRelatorioUseCase.gerarRelatorio()).thenReturn(expectedDto);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/telemetria")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("servicos", hasSize(1))
                .body("servicos[0].nome", is("Servico Teste"))
                .body("servicos[0].quantidadeChamadas", is(150))
                .body("periodo.inicio", is("2023-01-01"))
                .body("periodo.fim", is("2023-01-31"));

        Mockito.verify(gerarRelatorioUseCase, Mockito.times(1)).gerarRelatorio();
    }

    @Test
    @TestSecurity(user = "testuser", roles = "USER")
    void testObterTelemetria_SemRoleAdmin_Retorna403() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/telemetria")
                .then()
                .statusCode(403);

        Mockito.verify(gerarRelatorioUseCase, Mockito.never()).gerarRelatorio();
    }

    @Test
    void testObterTelemetria_SemAutenticacao_Retorna401() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/telemetria")
                .then()
                .statusCode(401);

        Mockito.verify(gerarRelatorioUseCase, Mockito.never()).gerarRelatorio();
    }

    @Test
    @TestSecurity(user = "testuser", roles = "ADMIN")
    void testObterTelemetria_ErroNoUseCase_Retorna500() {
        RuntimeException ex = new RuntimeException("Falha inesperada de teste");
        Mockito.when(gerarRelatorioUseCase.gerarRelatorio()).thenThrow(ex);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/telemetria")
                .then()
                .statusCode(500)
                .body("status", is(500))
                .body("error", is("Internal Error"))
                // CORREÇÃO: Ajuste para a mensagem genérica que seu GlobalExceptionMapper retorna
                .body("message", containsString("Erro inesperado ao processar a requisição"));

        Mockito.verify(gerarRelatorioUseCase, Mockito.times(1)).gerarRelatorio();
    }
}