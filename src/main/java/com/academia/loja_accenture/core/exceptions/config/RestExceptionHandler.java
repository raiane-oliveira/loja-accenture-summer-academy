package com.academia.loja_accenture.core.exceptions.config;

import com.academia.loja_accenture.core.api.responses.HttpApiErrorMessage;
import com.academia.loja_accenture.core.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(InvalidJsonException.class)
  private ResponseEntity<HttpApiErrorMessage> invalidJsonExceptionHandler(InvalidJsonException e) {
    return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
  }
  
  @ExceptionHandler(ResourceNotFound.class)
  private ResponseEntity<HttpApiErrorMessage> resourceNotFoundHandler(ResourceNotFound e) {
    return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  @ExceptionHandler(EstoqueNotFoundException.class)
  private ResponseEntity<HttpApiErrorMessage> estoqueNotFoundHandler(EstoqueNotFoundException e) {
    return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  @ExceptionHandler(PagamentoNotFoundException.class)
  private ResponseEntity<HttpApiErrorMessage> pagamentoNotFoundHandler(PagamentoNotFoundException e) {
    return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  @ExceptionHandler(PedidoNotFoundException.class)
  private ResponseEntity<HttpApiErrorMessage> pedidoNotFoundHandler(PedidoNotFoundException e) {
    return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  @ExceptionHandler(ProdutoNotFoundException.class)
  private ResponseEntity<HttpApiErrorMessage> produtoNotFoundHandler(ProdutoNotFoundException e) {
    return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  @ExceptionHandler(ClienteNotFoundException.class)
  private ResponseEntity<HttpApiErrorMessage> clienteNotFoundHandler(ClienteNotFoundException e) {
    return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  @ExceptionHandler(VendedorNotFoundException.class)
  private ResponseEntity<HttpApiErrorMessage> vendedorNotFoundHandler(VendedorNotFoundException e) {
    return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  private ResponseEntity<HttpApiErrorMessage> runtimeExceptionHandler(RuntimeException e) {
    return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
  }

  private ResponseEntity<HttpApiErrorMessage> getResponse(HttpStatus status, String message) {
    HttpApiErrorMessage response = new HttpApiErrorMessage(HttpStatus.BAD_REQUEST.value(), message);
    return ResponseEntity.status(status.value()).body(response);
  }
}
