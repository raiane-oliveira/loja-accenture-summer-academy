package com.academia.loja_accenture.modulos.usuario.repository;

import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
}
