package com.academia.loja_accenture.modulos.pedido.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CadastrarPedidoDTO(
        @NotNull Long clienteId,
        @NotNull List<Long> produtosIds
) {}