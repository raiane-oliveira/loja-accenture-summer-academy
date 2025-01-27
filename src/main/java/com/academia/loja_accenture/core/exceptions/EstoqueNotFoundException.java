package com.academia.loja_accenture.core.exceptions;

public class EstoqueNotFoundException extends RuntimeException {
  public EstoqueNotFoundException() {
    super("Estoque não encontrado");
  }

  public EstoqueNotFoundException(String message) {
    super(message);
  }
}
