package com.academia.loja_accenture.core.message_broker;

import lombok.Getter;

@Getter
public enum Queues {
  PEDIDOS_PAGOS("equipeum.pedidos.pagos"),
  ESTOQUE_PEDIDOS_PAGOS("equipeum.estoque.pedidos.pagos"),
  PEDIDOS_CANCELADOS("equipeum.pedidos.cancelados"),
  PEDIDOS_REGISTRADOS("equipeum.pedidos.registrados");
  
  private String name;
  
  Queues(String name) {
    this.name = name;
  }
}
