package com.academia.loja_accenture.modulos.pedido.repository;

import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
