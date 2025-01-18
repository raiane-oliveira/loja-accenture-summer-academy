package com.academia.loja_accenture.modulos.estoque.domain;

import lombok.Getter;

import java.util.Date;

@Getter
public class EstoqueTemProduto {
  private Long produtoId;
  private Long estoqueId;
  private Date dataAdicionado;
}
