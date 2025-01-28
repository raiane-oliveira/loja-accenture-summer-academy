package com.academia.loja_accenture.core.message_broker;

import lombok.Getter;

@Getter
public enum Exchanges {
  PEDIDOS("equipeum.pedido"),
  PEDIDOS_PAGOS("equipeum.pedido.pago");
  
  private String name;
  
  Exchanges(String name) {
    this.name = name;
  }
}
