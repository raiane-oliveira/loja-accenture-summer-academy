package com.academia.loja_accenture.modulos.usuario.controller;

import com.academia.loja_accenture.config.security.TokenService;
import com.academia.loja_accenture.factory.MakeVendedor;
import com.academia.loja_accenture.modulos.usuario.domain.User;
import com.academia.loja_accenture.modulos.usuario.domain.UserRole;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.dto.AtualizarVendedorDTO;
import com.academia.loja_accenture.modulos.usuario.dto.RegistrarVendedorDTO;
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
public class VendedorControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private VendedorRepository vendedorRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private TokenService tokenService;
    
    private Vendedor vendedor;
    private String token;
    
    @BeforeEach
    void setUp() {
        vendedorRepository.deleteAll();
        
        vendedor = vendedorRepository.save(MakeVendedor.create());
        token = tokenService.generateToken(new User(vendedor.getId(), vendedor.getEmail(), vendedor.getSenha(), UserRole.VENDEDOR));
    }
    
    @Test
    void deveCadastrarVendedor() throws Exception {
        RegistrarVendedorDTO requestDTO = new RegistrarVendedorDTO("Novo Vendedor", "Setor", "novo@vendedor.com", "123456");
        
        ResultActions response = mockMvc.perform(post("/api/vendedores")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)));
        
        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is(requestDTO.nome())))
            .andExpect(jsonPath("$.email", is(requestDTO.email())))
            .andExpect(jsonPath("$.id").isNotEmpty());
    }
    
    @Test
    void deveListarVendedores() throws Exception {
        // Criar um segundo vendedor
        vendedorRepository.save(new Vendedor("Outro Vendedor", "Vendas", "outro@vendedor.com", "123456"));
        
        ResultActions response = mockMvc.perform(get("/api/vendedores")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON));
        
        response.andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].nome", is(vendedor.getNome())))
            .andExpect(jsonPath("$[1].nome", is("Outro Vendedor")));
    }
    
    @Test
    void deveBuscarVendedorPorId() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/vendedores/{id}", vendedor.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON));
        
        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(vendedor.getId().intValue())))
            .andExpect(jsonPath("$.nome", is(vendedor.getNome())))
            .andExpect(jsonPath("$.email", is(vendedor.getEmail())));
    }
    
    @Test
    void deveRetornarErroSeVendedorNaoExistir() throws Exception {
        mockMvc.perform(get("/api/vendedores/{id}", 999L)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void deveAtualizarVendedor() throws Exception {
        AtualizarVendedorDTO requestDTO = new AtualizarVendedorDTO("Vendedor Atualizado", "Vendas");
        
        ResultActions response = mockMvc.perform(put("/api/vendedores/{id}", vendedor.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)));
        
        response.andExpect(status().isNoContent());
        
        Vendedor vendedorAtualizado = vendedorRepository.findById(vendedor.getId()).orElseThrow();
        assert vendedorAtualizado.getNome().equals(requestDTO.nome());
    }
}
