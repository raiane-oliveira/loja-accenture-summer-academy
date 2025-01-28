package com.academia.loja_accenture.config.security;

import com.academia.loja_accenture.modulos.usuario.domain.UserRole;

public record TokenPayload(Long id, UserRole role) {
}
