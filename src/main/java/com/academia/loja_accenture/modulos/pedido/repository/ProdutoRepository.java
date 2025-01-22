package com.academia.loja_accenture.modulos.pedido.repository;

import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ProdutoRepository extends JpaRepository<Produto, Long>, PagingAndSortingRepository<Produto, Long> {
  @Query("SELECT p FROM Produto p JOIN FETCH p.vendedor v WHERE v.id = :id")
  Page<Produto> findByOwnerId(@Param("id") Long id, Pageable pageable);
}
