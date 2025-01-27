package com.academia.loja_accenture.core.exceptions;

public class PagamentoNotFoundException extends RuntimeException {
  public PagamentoNotFoundException() {
    super("Pagamento não encontrado");
  }

  public PagamentoNotFoundException(String message) {
    super(message);
  }
}
