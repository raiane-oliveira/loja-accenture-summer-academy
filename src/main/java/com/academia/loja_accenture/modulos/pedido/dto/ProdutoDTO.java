package com.academia.loja_accenture.modulos.pedido.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoDTO (
        Long id,
        String nome,
        String descricao,
        BigDecimal valor,
        LocalDateTime createdAt,
        Long vendedorId
) {
}
