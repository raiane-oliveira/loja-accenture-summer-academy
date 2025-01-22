package com.academia.loja_accenture.modulos.estoque.domain;

import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name = "estoque")
@Entity
@Data
public class Estoque {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  private String name;
  
  @Column(nullable = false)
  private Long quantidade;
  
  @Column(name = "created_at", nullable = false)
  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt = LocalDateTime.now();
  
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "produto_id", unique = true)
  private Produto produto;
}
