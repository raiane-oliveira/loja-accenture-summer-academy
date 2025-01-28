package com.academia.loja_accenture.modulos.estoque.dto;

import lombok.Data;

@Data
public class EstoqueResponseDTO {
    private Long id;
    private String name;
    private Long quantidade;
    private String produtoNome;
}
