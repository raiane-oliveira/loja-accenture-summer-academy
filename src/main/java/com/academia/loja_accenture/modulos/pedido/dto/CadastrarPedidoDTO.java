package com.academia.loja_accenture.modulos.pedido.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CadastrarPedidoDTO(
    @NotNull(message = "O pedido precisa de um cliente") Long clienteId,
    @NotNull(message = "O pedido precisa de um vendedor") Long vendedorId,
    @Size(min = 1, message = "O pedido precisa de pelo menos um produto") List<ProdutoComQuantidadeDTO> produtos,
    @NotBlank(message = "O pedido precisa de uma descrição") String descricao
) {
}
