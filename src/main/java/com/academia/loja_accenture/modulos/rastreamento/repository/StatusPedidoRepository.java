package com.academia.loja_accenture.modulos.rastreamento.repository;

import com.academia.loja_accenture.modulos.rastreamento.domain.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusPedidoRepository extends JpaRepository<StatusPedido, Long> {
}