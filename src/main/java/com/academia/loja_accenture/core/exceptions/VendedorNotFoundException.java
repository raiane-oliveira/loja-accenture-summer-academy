package com.academia.loja_accenture.core.exceptions;

public class VendedorNotFoundException extends RuntimeException {
  public VendedorNotFoundException() {
    super("Vendedor não encontrado");
  }

  public VendedorNotFoundException(String message) {
    super(message);
  }
}
