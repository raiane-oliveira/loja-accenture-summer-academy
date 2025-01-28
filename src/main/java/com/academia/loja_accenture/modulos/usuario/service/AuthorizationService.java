package com.academia.loja_accenture.modulos.usuario.service;

import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.domain.User;
import com.academia.loja_accenture.modulos.usuario.domain.UserRole;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.repository.ClienteRepository;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {
  private final ClienteRepository clienteRepository;
  private final VendedorRepository vendedorRepository;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Cliente cliente = clienteRepository.findByEmail(username).orElse(null);
    if (cliente != null) {
      return new User(cliente.getId(), cliente.getEmail(), cliente.getSenha(), UserRole.CLIENTE);
    }

    Vendedor vendedor = vendedorRepository.findByEmail(username).orElse(null);
    if (vendedor != null) {
      return new User(vendedor.getId(), vendedor.getEmail(), vendedor.getSenha(), UserRole.VENDEDOR);
    }
    
    throw new UsernameNotFoundException("Usuário não encontrado");
  }
}
