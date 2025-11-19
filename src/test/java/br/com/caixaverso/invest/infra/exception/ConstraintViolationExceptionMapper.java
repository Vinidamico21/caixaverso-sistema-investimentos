package br.com.caixaverso.invest.infra.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ConstraintViolationExceptionMapper
 */
class ConstraintViolationExceptionMapperTest {

    @Test
    void deveConstruirResponseComMensagensDeViolacaoEPath() {
        // given
        ConstraintViolationExceptionMapper mapper = new ConstraintViolationExceptionMapper();

        UriInfo uriInfo = mock(UriInfo.class);
        when(uriInfo.getPath()).thenReturn("api/teste");
        // campo package-private, acessível no mesmo pacote
        mapper.uriInfo = uriInfo;

        // Violation 1
        ConstraintViolation<?> v1 = mock(ConstraintViolation.class);
        Path p1 = mock(Path.class);
        when(p1.toString()).thenReturn("campo1");
        when(v1.getPropertyPath()).thenReturn(p1);
        when(v1.getMessage()).thenReturn("mensagem 1");

        // Violation 2
        ConstraintViolation<?> v2 = mock(ConstraintViolation.class);
        Path p2 = mock(Path.class);
        when(p2.toString()).thenReturn("campo2");
        when(v2.getPropertyPath()).thenReturn(p2);
        when(v2.getMessage()).thenReturn("mensagem 2");

        Set<ConstraintViolation<?>> violations = new LinkedHashSet<>();
        violations.add(v1);
        violations.add(v2);

        ConstraintViolationException ex = new ConstraintViolationException(violations);

        // when
        Response response = mapper.toResponse(ex);

        // then
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        assertNotNull(response.getEntity());
        ApiErrorDTO body = (ApiErrorDTO) response.getEntity();

        // Se ApiErrorDTO for um record, os accessors serão status()/error()/message()/path()
        // (ajuste para getStatus()/getError()/getMessage()/getPath() se for POJO)
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), body.status());
        assertEquals("Validation Error", body.error());
        assertEquals("/api/teste", body.path());

        String msg = body.message();
        assertTrue(msg.contains("campo1: mensagem 1"));
        assertTrue(msg.contains("campo2: mensagem 2"));
        assertTrue(msg.contains(";")); // garante que o reduce concatenou com "; "
    }

    @Test
    void deveUsarMensagemPadraoEDefinirPathNuloQuandoSemViolacoesEUriInfoNulo() {
        // given
        ConstraintViolationExceptionMapper mapper = new ConstraintViolationExceptionMapper();
        mapper.uriInfo = null; // força branch do path() que retorna null

        ConstraintViolationException ex = new ConstraintViolationException(Set.of());

        // when
        Response response = mapper.toResponse(ex);

        // then
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        ApiErrorDTO body = (ApiErrorDTO) response.getEntity();
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), body.status());
        assertEquals("Validation Error", body.error());
        assertEquals("Dados inválidos", body.message());
        assertNull(body.path());
    }
}
