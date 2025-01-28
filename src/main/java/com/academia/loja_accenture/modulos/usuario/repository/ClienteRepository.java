package com.academia.loja_accenture.modulos.usuario.repository;

import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
  Optional<Cliente> findByEmail(String email);
}
