package com.academia.loja_accenture.core.exceptions;

public class ProdutoNotFoundException extends RuntimeException {
  public ProdutoNotFoundException() {
    super("Produto não encontrado");
  }

  public ProdutoNotFoundException(String message) {
    super(message);
  }
}
