package com.academia.loja_accenture.modulos.pagamento.domain;

import lombok.Getter;

@Getter
public enum MetodoPagamento {
  DEBITO("debito"),
  CREDITO("credito"),
  BOLETO("boleto"),
  PIX("pix");
  
  private String metodo;
  
  MetodoPagamento(String metodo) {
    this.metodo = metodo;
  }
}
