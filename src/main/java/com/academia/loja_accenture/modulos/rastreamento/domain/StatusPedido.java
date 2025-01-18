package com.academia.loja_accenture.modulos.rastreamento.domain;

import lombok.Getter;

import java.util.Date;

@Getter
public class StatusPedido {
  private Long id;
  private String descricao;
  private Date dataCriacao;
}
