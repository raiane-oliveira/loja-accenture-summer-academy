package com.academia.loja_accenture.modulos.usuario.domain;

import lombok.Getter;

@Getter
public enum UserRole {
  ADMIN("admin"),
  VENDEDOR("vendedor"),
  CLIENTE("cliente");
  
  private final String role;
  
  UserRole(String role) {
     this.role = role;
  }
}
