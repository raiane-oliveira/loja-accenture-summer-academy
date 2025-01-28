package com.academia.loja_accenture.modulos.pedido.repository;

import com.academia.loja_accenture.modulos.pedido.domain.PedidoTemProdutos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoTemProdutosRepository extends JpaRepository<PedidoTemProdutos, Long> {
}
