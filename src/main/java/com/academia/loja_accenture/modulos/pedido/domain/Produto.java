package com.academia.loja_accenture.modulos.pedido.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
public class Produto {
  private int id;
  private String nome;
  private String descricao;
  private BigDecimal valor;
  private Date dataCadastro;
}
