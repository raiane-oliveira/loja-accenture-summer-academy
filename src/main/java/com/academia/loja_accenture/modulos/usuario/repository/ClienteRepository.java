package com.academia.loja_accenture.modulos.usuario.repository;

import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
