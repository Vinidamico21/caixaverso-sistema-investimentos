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
import org.jboss.logging.Logger;

import java.sql.SQLException;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable ex) {

        String path = path();

        // 1) Exceções de domínio customizadas
        if (ex instanceof ApiException apiEx) {
            LOG.warnf("ApiException tratada | status=%d | error=%s | path=%s | msg=%s",
                    apiEx.getStatus().getStatusCode(),
                    apiEx.getError(),
                    path,
                    apiEx.getMessage());

            ApiErrorDTO body = new ApiErrorDTO(
                    apiEx.getStatus().getStatusCode(),
                    apiEx.getError(),
                    apiEx.getMessage(),
                    path
            );
            return Response.status(apiEx.getStatus()).entity(body).build();
        }

        // 2) Exceções SQL - tratamento genérico
        if (ex instanceof SQLException ||
                (ex.getCause() instanceof SQLException)) {

            return handleSqlException(ex, path);
        }

        // 3) Método HTTP errado
        if (ex instanceof NotAllowedException nae) {
            LOG.warnf("NotAllowedException tratada | path=%s | msg=%s",
                    path, nae.getMessage());

            ApiErrorDTO body = new ApiErrorDTO(
                    Response.Status.METHOD_NOT_ALLOWED.getStatusCode(),
                    "Method Not Allowed",
                    "Método HTTP não suportado para este endpoint.",
                    path
            );
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity(body).build();
        }

        // 4) WebApplicationException genérica
        if (ex instanceof WebApplicationException webEx) {
            int status = webEx.getResponse() != null
                    ? webEx.getResponse().getStatus()
                    : Response.Status.BAD_REQUEST.getStatusCode();

            if (status == Response.Status.BAD_REQUEST.getStatusCode()) {
                LOG.warnf("WebApplicationException BAD_REQUEST tratada | path=%s | msg=%s",
                        path, webEx.getMessage());

                ApiErrorDTO body = new ApiErrorDTO(
                        status,
                        "Invalid Request",
                        "Dados de requisição inválidos.",
                        path
                );
                return Response.status(status).entity(body).build();
            }
        }

        // 5) Erros de parsing / corpo / parâmetros inválidos
        if (ex instanceof BadRequestException || ex instanceof ProcessingException) {
            LOG.warnf("%s tratada como BAD_REQUEST | path=%s | msg=%s",
                    ex.getClass().getSimpleName(), path, ex.getMessage());

            ApiErrorDTO body = new ApiErrorDTO(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    "Invalid Request",
                    "Dados de requisição inválidos.",
                    path
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(body).build();
        }

        // 6) Bean Validation
        if (ex instanceof ConstraintViolationException cve) {
            String msg = cve.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .reduce((a, b) -> a + "; " + b)
                    .orElse("Dados inválidos");

            LOG.warnf("ConstraintViolationException tratada | path=%s | msg=%s",
                    path, msg);

            ApiErrorDTO body = new ApiErrorDTO(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    "Validation Error",
                    msg,
                    path
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(body).build();
        }

        // 7) 500 padrão
        LOG.errorf(ex, "Erro inesperado nao tratado | path=%s | exception=%s",
                path, ex.getClass().getSimpleName());

        ApiErrorDTO body = new ApiErrorDTO(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Internal Error",
                "Erro inesperado ao processar a requisição.",
                path
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(body).build();
    }

    private Response handleSqlException(Throwable ex, String path) {
        LOG.errorf(ex, "SQLException | path=%s | exception=%s | msg=%s",
                path, ex.getClass().getSimpleName(), ex.getMessage());

        String errorMessage = "Erro de banco de dados";

        // Extrair a causa SQL real
        SQLException sqlEx = null;
        if (ex instanceof SQLException) {
            sqlEx = (SQLException) ex;
        } else if (ex.getCause() instanceof SQLException) {
            sqlEx = (SQLException) ex.getCause();
        }

        if (sqlEx != null) {
            // Mensagem mais específica baseada no erro SQL
            if (sqlEx.getMessage().contains("Invalid object name")) {
                errorMessage = "Tabela ou objeto não encontrado no banco de dados";
            } else if (sqlEx.getMessage().contains("violat") || sqlEx.getMessage().contains("constraint")) {
                errorMessage = "Violação de regra do banco de dados";
            } else {
                errorMessage = String.format("Erro SQL: %s", sqlEx.getMessage());
            }
        }

        SqlException sqlException = new SqlException(errorMessage);

        ApiErrorDTO body = new ApiErrorDTO(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "DATABASE_ERROR",
                errorMessage,
                path
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(body).build();
    }

    private String path() {
        return uriInfo != null ? "/" + uriInfo.getPath() : null;
    }
}