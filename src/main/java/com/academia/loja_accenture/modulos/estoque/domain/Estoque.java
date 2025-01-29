package com.academia.loja_accenture.modulos.estoque.domain;

import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name = "estoque")
@Entity
@Data
@NoArgsConstructor
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

  @ManyToOne
  @JoinColumn(name = "produto_id", unique = true, nullable = false)
  private Produto produto;

  // Atualizar quantidade do estoque (incrementar/decrementar)
  public void atualizarQuantidade(Long quantidadeAlterada) {
    if (this.quantidade + quantidadeAlterada < 0) {
      throw new IllegalArgumentException("Quantidade insuficiente no estoque.");
    }
    this.quantidade += quantidadeAlterada;
  }
  
  public Estoque(Long id, Produto produto, Long quantidade, String name) {
    this.id = id;
    this.name = name;
    this.quantidade = quantidade;
    this.produto = produto;
  }
}
