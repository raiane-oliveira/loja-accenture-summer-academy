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
  
  @Column(name = "created_at", nullable = false)
  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;
  
  @ManyToMany(mappedBy = "historicoStatus")
  private List<Pedido> pedidos = new ArrayList<>();
}
