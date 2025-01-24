package com.academia.loja_accenture.factory;

import com.academia.loja_accenture.modulos.usuario.domain.Cliente;

public class MakeCliente {
  public static Cliente create() {
    Cliente vendedor = new Cliente();
    vendedor.setId(null);
    vendedor.setNome("John Doe");
    vendedor.setEmail("johndoe@example.com");
    vendedor.setSenha("123456");
    
    return vendedor;
  }
  
  public static Cliente create(Long id, String nome, String email, String senha) {
    Cliente vendedor = new Cliente();
    vendedor.setId(id);
    vendedor.setNome(nome);
    vendedor.setEmail(email);
    vendedor.setSenha(senha);
    
    return vendedor;
  }
}
