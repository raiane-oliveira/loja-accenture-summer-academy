package com.academia.loja_accenture.factory;

import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MakeProduto {
  public static Produto create() {
    Produto produto = new Produto();
    produto.setNome("Produto 1");
    produto.setDescricao("descrição produto 1");
    produto.setValor(BigDecimal.valueOf(100));
    produto.setCreatedAt(LocalDateTime.now());
    
    return produto;
  }
  
  public static Produto create(Long id) {
    Produto produto = new Produto();
    produto.setId(id);
    return produto;
  }
  
  public static Produto create(Long id, String nome, String descricao, BigDecimal valor, Vendedor vendedor) {
    Produto produto = new Produto();
    produto.setId(id);
    produto.setNome(nome);
    produto.setDescricao(descricao);
    produto.setValor(valor);
    produto.setVendedor(vendedor);
    produto.setCreatedAt(LocalDateTime.now());
    
    return produto;
  }
}
