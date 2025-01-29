package com.academia.loja_accenture.modulos.pedido.domain;

import com.academia.loja_accenture.modulos.estoque.domain.Estoque;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produto")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
  
  @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PedidoTemProdutos> pedidoTemProdutos = new ArrayList<>();
  
  public Produto(Long id, String nome, String descricao, BigDecimal valor) {
    this.id = id;
    this.nome = nome;
    this.descricao = descricao;
    this.valor = valor;
  }
}
