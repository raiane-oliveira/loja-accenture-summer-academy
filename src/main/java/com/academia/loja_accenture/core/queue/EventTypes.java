package com.academia.loja_accenture.core.queue;

import lombok.Getter;

@Getter
public enum EventTypes {
  PEDIDO_REGISTRADO("pedido-registrado");
  
  private String name;
  
  EventTypes(String name) {
    this.name = name;
  }
}
