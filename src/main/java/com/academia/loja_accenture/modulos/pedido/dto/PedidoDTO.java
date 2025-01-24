package com.academia.loja_accenture.modulos.pedido.dto;

import java.util.List;

public record PedidoDTO(
        Long id,
        Long clienteId,
        List<ProdutoDTO> produtos,
        Double total
) {}
