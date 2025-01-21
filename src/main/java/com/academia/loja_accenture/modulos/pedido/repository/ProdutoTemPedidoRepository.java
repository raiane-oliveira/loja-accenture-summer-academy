package com.academia.loja_accenture.modulos.pedido.repository;

import com.academia.loja_accenture.modulos.pedido.domain.ProdutoTemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoTemPedidoRepository extends JpaRepository<ProdutoTemPedido, Long> {
}
