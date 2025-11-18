package br.com.caixaverso.invest.infra.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable ex) {

        // 1) Exceções de domínio (Business, NotFound, Unauthorized, etc.)
        if (ex instanceof ApiException apiEx) {
            ApiErrorDTO body = new ApiErrorDTO(
                    apiEx.getStatus().getStatusCode(),
                    apiEx.getError(),
                    apiEx.getMessage(),
                    path()
            );
            return Response.status(apiEx.getStatus()).entity(body).build();
        }

        // 2) Método HTTP errado
        if (ex instanceof NotAllowedException) {
            ApiErrorDTO body = new ApiErrorDTO(
                    Response.Status.METHOD_NOT_ALLOWED.getStatusCode(),
                    "Method Not Allowed",
                    "Método HTTP não suportado para este endpoint.",
                    path()
            );
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity(body).build();
        }

        // 3) WebApplicationException genérica (ex.: JSON parse -> HTTP 400 Bad Request)
        if (ex instanceof WebApplicationException webEx) {
            int status = webEx.getResponse() != null
                    ? webEx.getResponse().getStatus()
                    : Response.Status.BAD_REQUEST.getStatusCode();

            if (status == Response.Status.BAD_REQUEST.getStatusCode()) {
                ApiErrorDTO body = new ApiErrorDTO(
                        status,
                        "Invalid Request",
                        "Dados de requisição inválidos.",
                        path()
                );
                return Response.status(status).entity(body).build();
            }
        }

        // 4) Erros de parsing / corpo / parâmetros inválidos
        if (ex instanceof BadRequestException || ex instanceof ProcessingException) {
            ApiErrorDTO body = new ApiErrorDTO(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    "Invalid Request",
                    "Dados de requisição inválidos.",
                    path()
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(body).build();
        }

        // 5) Bean Validation (@Valid, @NotNull, @Positive, etc.)
        if (ex instanceof ConstraintViolationException cve) {
            String msg = cve.getConstraintViolations().stream()
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

        // 6) 500 padrão
        ApiErrorDTO body = new ApiErrorDTO(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Internal Error",
                "Erro inesperado ao processar a requisição.",
                path()
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(body).build();
    }

    private String path() {
        return uriInfo != null ? "/" + uriInfo.getPath() : null;
    }
}
