package com.academia.loja_accenture.modulos.estoque.domain;

import lombok.Getter;

import java.util.Date;

@Getter
public class Estoque {
  private Long id;
  private String quantidade;
  private Date dataCadastro;
}
