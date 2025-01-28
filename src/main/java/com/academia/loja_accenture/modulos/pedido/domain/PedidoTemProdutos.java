package com.academia.loja_accenture.modulos.pedido.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "pedido_tem_produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoTemProdutos {
  @EmbeddedId
  private PedidoTemProdutosId id;
  
  @ManyToOne
  @MapsId("pedidoId")
  @JoinColumn(name = "pedido_id")
  private Pedido pedido;
  
  @ManyToOne
  @MapsId("produtoId")
  @JoinColumn(name = "produto_id")
  private Produto produto;
  
  @Column(name = "quantidade_produto", nullable = false)
  @ColumnDefault(value = "1")
  private int quantidadeProduto;
}
