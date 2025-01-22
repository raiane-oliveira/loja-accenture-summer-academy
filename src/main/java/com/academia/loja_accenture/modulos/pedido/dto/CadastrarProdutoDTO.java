package com.academia.loja_accenture.modulos.pedido.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CadastrarProdutoDTO (
    @NotBlank(message = "O nome do produto deve ser informado") String nome,
    @NotBlank(message = "A descrição do produto deve ser informada") String descricao,
    @Min(value = 0, message = "O valor do produto não pode ser negativo") BigDecimal valor,
    @NotNull(message = "O produto precisa ter um vendedor") Long vendedorId) {
}
