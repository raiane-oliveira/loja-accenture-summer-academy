package com.academia.loja_accenture.modulos.pedido.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "produto_tem_pedido")
@Data
public class ProdutoTemPedido {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  @ManyToOne
  @JoinColumn(name = "produto_id")
  private Produto produto;

  @ManyToOne
  @JoinColumn(name = "pedido_id")
  private Pedido pedido;
}
