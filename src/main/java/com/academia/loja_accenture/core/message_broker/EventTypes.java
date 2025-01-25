package com.academia.loja_accenture.core.message_broker;

import lombok.Getter;

@Getter
public enum EventTypes {
  PEDIDO_REGISTRADO("pedido-registrado");
  
  private String name;
  
  EventTypes(String name) {
    this.name = name;
  }
}
