package com.academia.loja_accenture.modulos.pedido.dto;

import java.time.LocalDateTime;

public record ProdutoDTO (
        Long id,
        String nome,
        String descricao,
        double valor,
        LocalDateTime createdAt,
        Long vendedorId
) {
}
