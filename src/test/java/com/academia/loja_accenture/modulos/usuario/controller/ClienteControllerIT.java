package com.academia.loja_accenture.modulos.usuario.controller;

import com.academia.loja_accenture.config.security.TokenService;
import com.academia.loja_accenture.factory.MakeCliente;
import com.academia.loja_accenture.factory.MakeVendedor;
import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.domain.User;
import com.academia.loja_accenture.modulos.usuario.domain.UserRole;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.dto.AtualizarClienteDTO;
import com.academia.loja_accenture.modulos.usuario.dto.RegistrarClienteDTO;
import com.academia.loja_accenture.modulos.usuario.repository.ClienteRepository;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
public class ClienteControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private VendedorRepository vendedorRepository;
    
    private String tokenCliente;
    private String tokenVendedor;
    private Cliente cliente;
    
    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();
        vendedorRepository.deleteAll();
        
        cliente = clienteRepository.save(MakeCliente.create());
        Vendedor vendedor = vendedorRepository.save(MakeVendedor.create());
        tokenCliente = tokenService.generateToken(new User(cliente.getId(), cliente.getEmail(), cliente.getSenha(), UserRole.CLIENTE));
        tokenVendedor = tokenService.generateToken(new User(vendedor.getId(), vendedor.getEmail(), vendedor.getSenha(), UserRole.VENDEDOR));
    }
    
    @Test
    void deveCadastrarCliente() throws Exception {
        RegistrarClienteDTO requestDTO = new RegistrarClienteDTO("Novo Cliente", "novo@cliente.com", "123456");
        
        ResultActions response = mockMvc.perform(post("/api/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)));
        
        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is(requestDTO.nome())))
            .andExpect(jsonPath("$.email", is(requestDTO.email())))
            .andExpect(jsonPath("$.id").isNotEmpty());
    }
    
    @Test
    void deveListarClientes() throws Exception {
        // Criar um segundo cliente
        clienteRepository.save(new Cliente("Outro Cliente", "outro@cliente.com", "123456"));
        
        ResultActions response = mockMvc.perform(get("/api/clientes")
            .header("Authorization", "Bearer " + tokenVendedor)
            .contentType(MediaType.APPLICATION_JSON));
        
        response.andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].nome", is(cliente.getNome())))
            .andExpect(jsonPath("$[1].nome", is("Outro Cliente")));
    }
    
    @Test
    void deveBuscarClientePorId() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/clientes/{id}", cliente.getId())
            .header("Authorization", "Bearer " + tokenCliente)
            .contentType(MediaType.APPLICATION_JSON));
        
        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(cliente.getId().intValue())))
            .andExpect(jsonPath("$.nome", is(cliente.getNome())))
            .andExpect(jsonPath("$.email", is(cliente.getEmail())));
    }
    
    @Test
    void deveRetornarErroSeClienteNaoExistir() throws Exception {
        mockMvc.perform(get("/api/clientes/{id}", 999L)
                .header("Authorization", "Bearer " + tokenCliente))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void deveAtualizarCliente() throws Exception {
        AtualizarClienteDTO requestDTO = new AtualizarClienteDTO("Cliente Atualizado");
        
        ResultActions response = mockMvc.perform(put("/api/clientes/{id}", cliente.getId())
            .header("Authorization", "Bearer " + tokenCliente)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)));
        
        response.andExpect(status().isNoContent());
        
        Cliente clienteAtualizado = clienteRepository.findById(cliente.getId()).orElseThrow();
        assert clienteAtualizado.getNome().equals(requestDTO.nome());
    }
}