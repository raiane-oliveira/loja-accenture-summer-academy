package com.academia.loja_accenture.modulos.pagamento.domain;

import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Classe Pagamento representa um pagamento realizado para um pedido no sistema.
 * Mapeia as informações de um pagamento, como o valor, o método de pagamento,
 * a data de pagamento e a confirmação do mesmo.
 *
 * @author Bruna Neves
 */
@Entity
@Table(name = "pagamento")
@Data
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private StatusPagamento status;
    
    @Column(nullable = false)
    private MetodoPagamento metodo;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    
    @Column(name = "data_pagamento", nullable = false)
    private LocalDateTime dataPagamento;
    
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id", unique = true, nullable = false)
    private Pedido pedido;
}
