package com.academia.loja_accenture.modulos.pedido.domain;

import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
  
  @Column(nullable = false)
  private BigDecimal valor;
  
  @Column(name = "created_at", nullable = false)
  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;
  
  @ManyToOne
  @JoinColumn(name = "vendedor_id")
  private Vendedor vendedor;
}
