package com.academia.loja_accenture.modulos.pedido.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoTemProdutosId implements Serializable {
  @Column(name = "pedido_id")
  private Long pedidoId;
  
  @Column(name = "produto_id")
  private Long produtoId;
  
  // Override equals and hashCode
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PedidoTemProdutosId that = (PedidoTemProdutosId) o;
    return Objects.equals(pedidoId, that.pedidoId) && Objects.equals(produtoId, that.produtoId);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(pedidoId, produtoId);
  }
}
