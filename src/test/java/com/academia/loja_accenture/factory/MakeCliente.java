package com.academia.loja_accenture.factory;

import com.academia.loja_accenture.modulos.usuario.domain.Cliente;

public class MakeCliente {
  public static Cliente create() {
    Cliente cliente = new Cliente();
    cliente.setId(null);
    cliente.setNome("John Doe");
    cliente.setEmail("cliente@example.com");
    cliente.setSenha("123456");
    
    return cliente;
  }
  
  public static Cliente create(Long id) {
    Cliente cliente = new Cliente();
    cliente.setId(id);
    return cliente;
  }
  
  public static Cliente create(Long id, String nome, String email, String senha) {
    Cliente cliente = new Cliente();
    cliente.setId(id);
    cliente.setNome(nome);
    cliente.setEmail(email);
    cliente.setSenha(senha);
    
    return cliente;
  }
}
