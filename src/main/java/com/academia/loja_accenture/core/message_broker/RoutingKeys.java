package com.academia.loja_accenture.core.message_broker;

import lombok.Getter;

@Getter
public enum RoutingKeys {
  PEDIDO_REGISTRADO("pedido.evento.registrar"),;
  
  private String name;
  
  RoutingKeys(String name) {
    this.name = name;
  }
}
