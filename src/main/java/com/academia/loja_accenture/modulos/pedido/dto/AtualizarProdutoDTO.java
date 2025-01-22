package com.academia.loja_accenture.modulos.pedido.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

public record AtualizarProdutoDTO(
    @Nullable @NotEmpty String nome,
    @Nullable @NotEmpty String descricao,
    @Nullable @Min(0) BigDecimal valor
    ) {
}
