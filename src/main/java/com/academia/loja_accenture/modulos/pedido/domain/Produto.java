package com.academia.loja_accenture.modulos.pedido.domain;

import com.academia.loja_accenture.modulos.estoque.domain.Estoque;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "produto")
@Data
public class Produto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  private String nome;
  
  @Column(nullable = false)
  private String descricao;
  
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal valor;
  
  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;
  
  @ManyToOne
  @JoinColumn(name = "vendedor_id", nullable = false)
  private Vendedor vendedor;
  
  @OneToMany(mappedBy = "produto")
  private List<Estoque> estoques;
  
//  @ManyToMany(mappedBy = "produtos")
//  private Set<Pedido> pedidos = new HashSet<>();

  @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PedidoTemProdutos> pedidoTemProdutos = new ArrayList<>();
  
//  public void addPedido(Pedido pedido) {
//    PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
//    pedidoTemProdutos.setPedido(pedido);
//    pedidoTemProdutos.setProduto(this);
//
//    this.pedidoTemProdutos.add(pedidoTemProdutos);
//    pedido.getPedidoTemProdutos().add(pedidoTemProdutos);
//  }
}
