package com.academia.loja_accenture.modulos.estoque.repository;

import com.academia.loja_accenture.modulos.estoque.domain.EstoqueTemProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueTemProdutoRepository extends JpaRepository<EstoqueTemProduto, Long> {
}
