package com.academia.loja_accenture.modulos.estoque.repository;

import com.academia.loja_accenture.modulos.estoque.domain.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
}
