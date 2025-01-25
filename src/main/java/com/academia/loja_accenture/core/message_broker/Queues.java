package com.academia.loja_accenture.core.message_broker;

import lombok.Getter;

@Getter
public enum Queues {
//  STATUS_PEDIDOS("equipeum:status-pedidos"),
  PEDIDOS_REGISTRADOS("equipeum:pedidos-registrados");
  
  private String name;
  
  Queues(String name) {
    this.name = name;
  }
}
