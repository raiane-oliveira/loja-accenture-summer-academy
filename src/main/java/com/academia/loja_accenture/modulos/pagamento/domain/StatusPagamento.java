package com.academia.loja_accenture.modulos.pagamento.domain;

import lombok.Getter;

@Getter
public enum StatusPagamento {
  PROCESSANDO("processando"),
  FINALIZADO("finalizado"),
  CANCELADO("cancelado");
  
  private String status;
  
  StatusPagamento(String status) {
    this.status = status;
  }
}
