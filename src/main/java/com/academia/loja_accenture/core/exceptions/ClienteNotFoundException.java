package com.academia.loja_accenture.core.exceptions;

public class ClienteNotFoundException extends RuntimeException {
  public ClienteNotFoundException() {
    super("Cliente não encontrado");
  }

  public ClienteNotFoundException(String message) {
    super(message);
  }
}
