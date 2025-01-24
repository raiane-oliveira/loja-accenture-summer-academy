package com.academia.loja_accenture.modulos.pagamento.dto;

import com.academia.loja_accenture.modulos.pagamento.domain.MetodoPagamento;
import com.academia.loja_accenture.modulos.pagamento.domain.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoDTO(
    Long id,
    StatusPagamento status,
    MetodoPagamento metodo,
    BigDecimal valor,
    LocalDateTime dataPagamento,
    Long pedidoId,
    LocalDateTime createdAt
) {
}
