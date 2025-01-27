package com.academia.loja_accenture.core.exceptions;

public class PedidoNotFoundException extends RuntimeException {
  public PedidoNotFoundException() {
    super("Pedido não encontrado");
  }

  public PedidoNotFoundException(String message) {
    super(message);
  }
}
