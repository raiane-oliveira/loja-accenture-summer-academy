package com.academia.loja_accenture.modulos.pedido.repository;

import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
