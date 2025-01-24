package com.academia.loja_accenture.core.queue;

import lombok.Getter;

@Getter
public enum RabbitQueue {
//  STATUS_PEDIDOS("equipe_um_status-pedidos"),
  PEDIDOS_REGISTRADOS("equipeum:pedidos-registrados");
  
  private String name;
  
  RabbitQueue(String name) {
    this.name = name;
  }
}
