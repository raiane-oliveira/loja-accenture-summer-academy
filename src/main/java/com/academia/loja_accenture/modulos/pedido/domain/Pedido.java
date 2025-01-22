package com.academia.loja_accenture.modulos.pedido.domain;

import com.academia.loja_accenture.modulos.pagamento.domain.Pagamento;
import com.academia.loja_accenture.modulos.rastreamento.domain.StatusPedido;
import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pedido")
@Data
public class Pedido {
  @Id
  private Long id;
  
  @Column(nullable = false)
  private String descricao;
  
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal valor;
  
  @Column(nullable = false)
  private int quantidade;
  
  @Column(name = "created_at", nullable = false)
  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;
  
  @ManyToOne
  @JoinColumn(name = "cliente_id")
  private Cliente cliente;
  
  @ManyToOne
  @JoinColumn(name = "vendedor_id")
  private Vendedor vendedor;
  
  @ManyToMany(mappedBy = "pedidos")
  private List<Produto> produtos = new ArrayList<>();
  
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "pedido_historico_status",
      joinColumns = @JoinColumn(name = "pedido_id"),
      inverseJoinColumns = @JoinColumn(name = "status_pedido_id")
  )
  private Set<StatusPedido> historicoStatus = new HashSet<>();
  
  @OneToOne(mappedBy = "pedido")
  private Pagamento pagamento;
}
