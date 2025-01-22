package com.academia.loja_accenture.modulos.pedido.domain;

import com.academia.loja_accenture.modulos.estoque.domain.Estoque;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
  
  @Column(name = "created_at", nullable = false)
  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;
  
  @ManyToOne
  @JoinColumn(name = "vendedor_id", nullable = false)
  private Vendedor vendedor;
  
  @ManyToOne
  @JoinColumn(name = "estoque_id", nullable = false)
  private Estoque estoque;
  
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "produto_tem_pedido",
      joinColumns = @JoinColumn(name = "produto_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "pedido_id", nullable = false)
  )
  private List<Pedido> pedidos = new ArrayList<>();
}
