package br.com.caixaverso.invest.infra.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException ex) {

        String msg = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Dados inválidos");

        ApiErrorDTO body = new ApiErrorDTO(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Validation Error",
                msg,
                path()
        );

        return Response.status(Response.Status.BAD_REQUEST).entity(body).build();
    }

    private String path() {
        return uriInfo != null ? "/" + uriInfo.getPath() : null;
    }
}
