package br.com.seuempresa.invest.resource;

import br.com.caixaverso.invest.application.dto.AuthRequestDTO;
import br.com.caixaverso.invest.application.dto.AuthResponseDTO;
import br.com.caixaverso.invest.application.port.in.AuthUseCase;
import br.com.caixaverso.invest.infra.exception.BusinessException;
import br.com.caixaverso.invest.infra.exception.UnauthorizedException;
import br.com.caixaverso.invest.resource.AuthResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(AuthResource.class)
class AuthResourceTest {

    @InjectMock
    AuthUseCase authUseCase;

    @Test
    void testLoginSuccess() {
        AuthResponseDTO mockResponse = new AuthResponseDTO("token123", "Bearer", 3600L);
        when(authUseCase.autenticar(any(AuthRequestDTO.class))).thenReturn(mockResponse);

        AuthRequestDTO req = new AuthRequestDTO("user", "pass");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(req)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("token", is("token123"))
                .body("tokenType", is("Bearer"))
                // usa qualquer número inteiro para evitar mismatch int/long
                .body("expiresIn", is(3600));

        verify(authUseCase, times(1)).autenticar(any(AuthRequestDTO.class));
        verifyNoMoreInteractions(authUseCase);
    }

    @Test
    void testLoginUnauthorized() {
        when(authUseCase.autenticar(any(AuthRequestDTO.class)))
                .thenThrow(new UnauthorizedException("Credenciais inválidas"));

        AuthRequestDTO req = new AuthRequestDTO("wrong", "creds");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(req)
                .when()
                .post("/login")
                .then()
                .statusCode(401)
                .body("message", anyOf(nullValue(), containsStringIgnoringCase("inválid")));

        verify(authUseCase, times(1)).autenticar(any(AuthRequestDTO.class));
        verifyNoMoreInteractions(authUseCase);
    }

    @Test
    void testLoginBadRequest() {
        when(authUseCase.autenticar(any(AuthRequestDTO.class)))
                .thenThrow(new BusinessException("Dados obrigatórios ausentes"));

        // username vazio -> deve virar 400 pelo exception mapper/validação
        AuthRequestDTO req = new AuthRequestDTO("", "pass");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(req)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .body("message", anyOf(nullValue(), containsStringIgnoringCase("ausentes")));

        verify(authUseCase, times(1)).autenticar(any(AuthRequestDTO.class));
        verifyNoMoreInteractions(authUseCase);
    }
}
