package com.academia.loja_accenture.modulos.estoque.repository;

import com.academia.loja_accenture.modulos.estoque.domain.Estoque;
import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
  @Query("SELECT e FROM Estoque e WHERE e.produto.id = :produtoId")
  Optional<Estoque> findByProdutoId(@Param("produtoId") Long produtoId);
}
