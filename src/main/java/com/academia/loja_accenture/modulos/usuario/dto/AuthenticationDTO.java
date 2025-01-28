package com.academia.loja_accenture.modulos.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record AuthenticationDTO(
    @Email(message = "E-mail inválido") String email,
    @NotNull(message = "Senha inválida") String password
    ) {
}
