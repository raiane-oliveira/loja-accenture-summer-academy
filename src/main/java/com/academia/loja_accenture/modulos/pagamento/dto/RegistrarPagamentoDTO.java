package com.academia.loja_accenture.modulos.pagamento.dto;

import com.academia.loja_accenture.modulos.pagamento.domain.MetodoPagamento;
import com.academia.loja_accenture.modulos.pagamento.domain.StatusPagamento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RegistrarPagamentoDTO(
    @NotNull StatusPagamento status,
    @NotNull MetodoPagamento metodo,
    @NotNull @Min(0) BigDecimal valor,
    @NotNull Long pedidoId
    ) {
}
