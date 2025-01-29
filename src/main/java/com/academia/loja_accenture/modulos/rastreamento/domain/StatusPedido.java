package com.academia.loja_accenture.modulos.rastreamento.domain;

import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "status_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusPedido {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StatusEnum status;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "pedido_id", nullable = false)
  @JsonIgnore // Evita problemas de serialização (recursão infinita)
  private Pedido pedido;
  
  public StatusPedido(StatusEnum status, Pedido pedido) {
    this.status = status;
    this.pedido = pedido;
  }
}
