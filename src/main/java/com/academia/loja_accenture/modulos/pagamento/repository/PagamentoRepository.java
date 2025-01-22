package com.academia.loja_accenture.modulos.pagamento.repository;

import com.academia.loja_accenture.modulos.pagamento.domain.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
