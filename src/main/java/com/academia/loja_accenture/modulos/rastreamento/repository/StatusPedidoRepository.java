package com.academia.loja_accenture.modulos.rastreamento.repository;

import com.academia.loja_accenture.modulos.rastreamento.domain.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusPedidoRepository extends JpaRepository<StatusPedido, Long> {

    Optional<StatusPedido> findTopByPedidoIdOrderByCreatedAtDesc(Long pedidoId);
    List<StatusPedido> findByPedidoIdOrderByCreatedAtDesc(Long pedidoId);
}
