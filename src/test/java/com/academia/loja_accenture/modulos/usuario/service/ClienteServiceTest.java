package com.academia.loja_accenture.modulos.usuario.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.academia.loja_accenture.core.exceptions.ClienteNotFoundException;
import com.academia.loja_accenture.core.exceptions.InvalidCredentialsException;
import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.dto.AtualizarClienteDTO;
import com.academia.loja_accenture.modulos.usuario.dto.ClienteDTO;
import com.academia.loja_accenture.modulos.usuario.dto.RegistrarClienteDTO;
import com.academia.loja_accenture.modulos.usuario.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    
    @Mock
    private ClienteRepository clienteRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private ClienteService clienteService;
    
    private RegistrarClienteDTO registrarClienteDTO;
    private AtualizarClienteDTO atualizarClienteDTO;
    private Cliente cliente;
    
    @BeforeEach
    void setUp() {
        registrarClienteDTO = new RegistrarClienteDTO("Cliente Teste", "teste@example.com", "senha123");
        atualizarClienteDTO = new AtualizarClienteDTO("Cliente Atualizado");
        
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");
        cliente.setEmail("teste@example.com");
        cliente.setSenha("senhaCriptografada");
    }
    
    @Test
    void save_validRequest_shouldReturnClienteDTO() {
        when(clienteRepository.findByEmail("teste@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        
        ClienteDTO response = clienteService.save(registrarClienteDTO);
        
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Cliente Teste");
        assertThat(response.email()).isEqualTo("teste@example.com");
        
        verify(clienteRepository, times(1)).findByEmail("teste@example.com");
        verify(passwordEncoder, times(1)).encode("senha123");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
    
    @Test
    void save_emailAlreadyExists_shouldThrowInvalidCredentialsException() {
        // Arrange
        when(clienteRepository.findByEmail("teste@example.com")).thenReturn(Optional.of(cliente));
        
        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> clienteService.save(registrarClienteDTO));
        verify(clienteRepository, times(1)).findByEmail("teste@example.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }
    
    @Test
    void update_validRequest_shouldUpdateCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        
        clienteService.update(1L, atualizarClienteDTO);
        
        assertThat(cliente.getNome()).isEqualTo("Cliente Atualizado");
        
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(cliente);
    }
    
    @Test
    void update_clienteNotFound_shouldThrowClienteNotFoundException() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ClienteNotFoundException.class, () -> clienteService.update(1L, atualizarClienteDTO));
        verify(clienteRepository, times(1)).findById(1L);
        verify(passwordEncoder, never()).encode(anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }
    
    @Test
    void getById_validId_shouldReturnClienteDTO() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        
        ClienteDTO response = clienteService.getById(1L);
        
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Cliente Teste");
        assertThat(response.email()).isEqualTo("teste@example.com");
        
        verify(clienteRepository, times(1)).findById(1L);
    }
    
    @Test
    void getById_clienteNotFound_shouldThrowClienteNotFoundException() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ClienteNotFoundException.class, () -> clienteService.getById(1L));
        verify(clienteRepository, times(1)).findById(1L);
    }
    
    @Test
    void listarTodos_shouldReturnListOfClienteDTO() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        
        List<ClienteDTO> response = clienteService.listarTodos();
        
        assertThat(response).hasSize(1);
        assertThat(response.getFirst().id()).isEqualTo(1L);
        assertThat(response.getFirst().nome()).isEqualTo("Cliente Teste");
        assertThat(response.getFirst().email()).isEqualTo("teste@example.com");
        
        verify(clienteRepository, times(1)).findAll();
    }
    
    @Test
    void listarTodos_semClientes_shouldReturnEmptyList() {
        when(clienteRepository.findAll()).thenReturn(List.of());
        
        List<ClienteDTO> response = clienteService.listarTodos();
        
        assertThat(response).isEmpty();
        verify(clienteRepository, times(1)).findAll();
    }
}