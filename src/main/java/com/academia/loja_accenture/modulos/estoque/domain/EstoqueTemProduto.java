package com.academia.loja_accenture.modulos.estoque.domain;

import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "estoque_tem_produto")
@Data
public class EstoqueTemProduto {
  @Id
  @ManyToOne
  @JoinColumn(name = "estoque_id")
  private Estoque estoque;
  
  @ManyToOne
  @JoinColumn(name = "produto_id")
  private Produto produto;
  
  @Column(name = "created_at", nullable = false)
  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;
}
