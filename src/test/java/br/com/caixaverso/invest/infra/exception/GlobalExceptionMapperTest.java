package br.com.caixaverso.invest.infra.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionMapperTest {

    // ----------------------------------------------------
    // helper: injeta um UriInfo com path no mapper
    // ----------------------------------------------------
    private GlobalExceptionMapper mapperWithPath(String path) throws Exception {
        GlobalExceptionMapper mapper = new GlobalExceptionMapper();

        UriInfo uriInfo = mock(UriInfo.class);
        when(uriInfo.getPath()).thenReturn(path);

        Field f = GlobalExceptionMapper.class.getDeclaredField("uriInfo");
        f.setAccessible(true);
        f.set(mapper, uriInfo);

        return mapper;
    }

    // ----------------------------------------------------
    // 1) ApiException (ex.: BusinessException)
    // ----------------------------------------------------
    @Test
    void deveTratarApiException() throws Exception {
        GlobalExceptionMapper mapper = mapperWithPath("api/test");

        BusinessException ex = new BusinessException("Erro de negócio");

        Response resp = mapper.toResponse(ex);

        assertEquals(ex.getStatus().getStatusCode(), resp.getStatus());
        assertInstanceOf(ApiErrorDTO.class, resp.getEntity());

        ApiErrorDTO body = (ApiErrorDTO) resp.getEntity();
        assertEquals(ex.getStatus().getStatusCode(), body.status());
        assertEquals(ex.getError(), body.error());
        assertEquals(ex.getMessage(), body.message());
        assertEquals("/api/test", body.path());
    }

    // ----------------------------------------------------
    // 2) NotAllowedException → 405 Method Not Allowed
    // ----------------------------------------------------
    @Test
    void deveTratarNotAllowedExceptionComoMethodNotAllowed() throws Exception {
        GlobalExceptionMapper mapper = mapperWithPath("api/simulacao");

        NotAllowedException ex = new NotAllowedException(
                "Método não permitido",
                Response.status(Response.Status.METHOD_NOT_ALLOWED).build()
        );

        Response resp = mapper.toResponse(ex);

        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), resp.getStatus());
        ApiErrorDTO body = (ApiErrorDTO) resp.getEntity();

        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), body.status());
        assertEquals("Method Not Allowed", body.error());
        assertEquals("Método HTTP não suportado para este endpoint.", body.message());
        assertEquals("/api/simulacao", body.path());
    }

    // ----------------------------------------------------
    // 3) WebApplicationException 400 (ex.: BadRequestException)
    // ----------------------------------------------------
    @Test
    void deveTratarWebApplicationExceptionBadRequestComoInvalidRequest() throws Exception {
        GlobalExceptionMapper mapper = mapperWithPath("api/json");

        WebApplicationException ex = new BadRequestException("JSON inválido");

        Response resp = mapper.toResponse(ex);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        ApiErrorDTO body = (ApiErrorDTO) resp.getEntity();

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), body.status());
        assertEquals("Invalid Request", body.error());
        assertEquals("Dados de requisição inválidos.", body.message());
        assertEquals("/api/json", body.path());
    }

    // ----------------------------------------------------
    // 4) ProcessingException → também cai como 400 Invalid Request
    // ----------------------------------------------------
    @Test
    void deveTratarProcessingExceptionComoBadRequest() throws Exception {
        GlobalExceptionMapper mapper = mapperWithPath("api/process");

        ProcessingException ex = new ProcessingException("Erro de processamento");

        Response resp = mapper.toResponse(ex);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        ApiErrorDTO body = (ApiErrorDTO) resp.getEntity();

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), body.status());
        assertEquals("Invalid Request", body.error());
        assertEquals("Dados de requisição inválidos.", body.message());
        assertEquals("/api/process", body.path());
    }

    // ----------------------------------------------------
    // helper: ConstraintViolation fake para testar lambda
    // ----------------------------------------------------
    private ConstraintViolation<Object> violation(String property, String message) {
        return new ConstraintViolation<>() {
            @Override
            public String getMessage() { return message; }

            @Override
            public String getMessageTemplate() { return null; }

            @Override
            public Object getRootBean() { return null; }

            @Override
            public Class<Object> getRootBeanClass() { return Object.class; }

            @Override
            public Object getLeafBean() { return null; }

            @Override
            public Object[] getExecutableParameters() { return new Object[0]; }

            @Override
            public Object getExecutableReturnValue() { return null; }

            @Override
            public Path getPropertyPath() {
                return new Path() {
                    @Override
                    public Iterator<Node> iterator() {
                        return Collections.emptyIterator();
                    }

                    @Override
                    public String toString() {
                        return property;
                    }
                };
            }

            @Override
            public Object getInvalidValue() { return null; }

            @Override
            public ConstraintDescriptor<?> getConstraintDescriptor() { return null; }

            @Override
            public <U> U unwrap(Class<U> type) {
                throw new UnsupportedOperationException("unwrap not supported");
            }
        };
    }

    // ----------------------------------------------------
    // 5) ConstraintViolationException → "Validation Error"
    // ----------------------------------------------------
    @Test
    void deveTratarConstraintViolationException() throws Exception {
        GlobalExceptionMapper mapper = mapperWithPath("api/validacao");

        ConstraintViolationException ex = new ConstraintViolationException(
                "erro de validação",
                Set.of(
                        violation("clienteId", "não pode ser nulo"),
                        violation("valor", "deve ser positivo")
                )
        );

        Response resp = mapper.toResponse(ex);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        ApiErrorDTO body = (ApiErrorDTO) resp.getEntity();

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), body.status());
        assertEquals("Validation Error", body.error());
        assertTrue(body.message().contains("clienteId"));
        assertTrue(body.message().contains("valor"));
        assertEquals("/api/validacao", body.path());
    }

    // ----------------------------------------------------
    // 6) Exceção genérica → 500, com uriInfo == null (branch else de path())
    // ----------------------------------------------------
    @Test
    void deveTratarErroGenericoComoInternalServerError() {
        GlobalExceptionMapper mapper = new GlobalExceptionMapper(); // uriInfo continua null

        RuntimeException ex = new RuntimeException("falha inesperada");

        Response resp = mapper.toResponse(ex);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), resp.getStatus());
        ApiErrorDTO body = (ApiErrorDTO) resp.getEntity();

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), body.status());
        assertEquals("Internal Error", body.error());
        assertEquals("Erro inesperado ao processar a requisição.", body.message());
        // como uriInfo é null, o path também é null
        assertNull(body.path());
    }
}
