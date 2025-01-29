package com.academia.loja_accenture.factory;

import com.academia.loja_accenture.modulos.pedido.domain.Pedido;

import java.math.BigDecimal;

public class MakePedido {
  public static Pedido create() {
    Pedido pedido = new Pedido();
    pedido.setId(null);
    pedido.setDescricao("Pedido Teste");
    pedido.setQuantidade(1);
    pedido.setValor(BigDecimal.valueOf(100.00));
    pedido.setPagamento(MakePagamento.create(1L));
    pedido.setVendedor(MakeVendedor.create(1L));
    pedido.setCliente(MakeCliente.create(1L));
    pedido.addProduto(MakeProduto.create(1L), 1);
    pedido.setValor(BigDecimal.TEN);
    
    return pedido;
  }
  
  public static Pedido create(Long id) {
    Pedido pedido = new Pedido();
    pedido.setId(id);
    return pedido;
  }
  
  public static Pedido create(Long id, String descricao, int quantidade, BigDecimal valor, Long vendedor, Long cliente) {
    Pedido pedido = new Pedido();
    pedido.setId(id);
    pedido.setDescricao(descricao);
    pedido.setQuantidade(quantidade);
    pedido.setValor(valor);
    pedido.setPagamento(MakePagamento.create(1L));
    pedido.setVendedor(MakeVendedor.create(vendedor));
    pedido.setCliente(MakeCliente.create(cliente));
    pedido.addProduto(MakeProduto.create(1L), 1);
    pedido.setValor(valor);
    
    return pedido;
  }
}
