package com.academia.loja_accenture.modulos.rastreamento.repository;

import com.academia.loja_accenture.modulos.rastreamento.domain.PedidoStatusHistorico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoStatusHistoricoRepository extends JpaRepository<PedidoStatusHistorico, Long> {
}
