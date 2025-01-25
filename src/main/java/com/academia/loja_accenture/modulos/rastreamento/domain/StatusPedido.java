package com.academia.loja_accenture.modulos.rastreamento.domain;

import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "status_pedido")
@Data
public class StatusPedido {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String descricao;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToMany(mappedBy = "historicoStatus")
  private List<Pedido> pedidos = new ArrayList<>();

  public StatusPedido() {}

  public StatusPedido(Long id, String descricao, LocalDateTime createdAt, List<Pedido> pedidos) {
    this.id = id;
    this.descricao = descricao;
    this.createdAt = createdAt;
    this.pedidos = pedidos;
  }
}
