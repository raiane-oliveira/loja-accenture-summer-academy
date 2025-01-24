package com.academia.loja_accenture.modulos.pagamento.dto;

import com.academia.loja_accenture.modulos.pagamento.domain.MetodoPagamento;
import com.academia.loja_accenture.modulos.pagamento.domain.StatusPagamento;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

public record AtualizarPagamentoDTO(
    @Nullable @NotEmpty StatusPagamento status,
    @Nullable @NotEmpty MetodoPagamento metodo,
    @Nullable @NotEmpty LocalDateTime dataPagamento
    ) {
}
