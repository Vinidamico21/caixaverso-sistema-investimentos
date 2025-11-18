 package br.com.caixaverso.invest.infra.exception;

import jakarta.ws.rs.core.Response;

 public abstract class ApiException extends RuntimeException {

     private final Response.Status status;
     private final String error;

     protected ApiException(Response.Status status, String error, String message) {
         super(message);
         this.status = status;
         this.error = error;
     }

     public Response.Status getStatus() { return status; }
     public String getError() { return error; }
 }