package com.academia.loja_accenture.factory;

import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;

public class MakeVendedor {
  public static Vendedor create() {
    Vendedor vendedor = new Vendedor();
    vendedor.setId(null);
    vendedor.setNome("John Doe");
    vendedor.setEmail("vendedor@example.com");
    vendedor.setSenha("123456");
    vendedor.setSetor("teste");
    
    return vendedor;
  }
  
  public static Vendedor create(Long id) {
    Vendedor vendedor = new Vendedor();
    vendedor.setId(id);
    return vendedor;
  }
  
  public static Vendedor create(Long id, String nome, String email, String senha, String setor) {
    Vendedor vendedor = new Vendedor();
    vendedor.setId(id);
    vendedor.setNome(nome);
    vendedor.setEmail(email);
    vendedor.setSenha(senha);
    vendedor.setSetor(setor);
    
    return vendedor;
  }
}
