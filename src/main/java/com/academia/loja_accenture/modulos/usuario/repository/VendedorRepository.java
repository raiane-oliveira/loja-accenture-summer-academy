package com.academia.loja_accenture.modulos.usuario.repository;

import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
  Optional<Vendedor> findByEmail(String email);
}
