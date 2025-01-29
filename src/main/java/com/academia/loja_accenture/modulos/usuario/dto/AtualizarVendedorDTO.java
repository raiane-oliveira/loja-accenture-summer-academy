package com.academia.loja_accenture.modulos.usuario.dto;

import jakarta.validation.constraints.Size;

public record AtualizarVendedorDTO(
        @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres.")
        String nome,

        String setor
) {}
