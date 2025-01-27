package com.academia.loja_accenture.modulos.usuario.controller;

import com.academia.loja_accenture.modulos.usuario.dto.RegistrarVendedorDTO;
import com.academia.loja_accenture.modulos.usuario.dto.VendedorDTO;
import com.academia.loja_accenture.modulos.usuario.service.VendedorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendedorController.class)
class VendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendedorService vendedorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCadastrarVendedor() throws Exception {
        RegistrarVendedorDTO dto = new RegistrarVendedorDTO("Carlos", "Vendas", "carlos@gmail.com", "senha123");
        VendedorDTO response = new VendedorDTO(1L, "Carlos", "Vendas", "carlos@gmail.com");

        when(vendedorService.save(Mockito.any(RegistrarVendedorDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/vendedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Carlos"))
                .andExpect(jsonPath("$.setor").value("Vendas"))
                .andExpect(jsonPath("$.email").value("carlos@gmail.com"));
    }

    @Test
    void deveListarVendedores() throws Exception {
        List<VendedorDTO> vendedores = Arrays.asList(
                new VendedorDTO(1L, "Carlos", "Vendas", "carlos@gmail.com"),
                new VendedorDTO(2L, "Ana", "Marketing", "ana@gmail.com")
        );

        when(vendedorService.listarTodos()).thenReturn(vendedores);

        mockMvc.perform(get("/api/vendedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Carlos"))
                .andExpect(jsonPath("$[1].nome").value("Ana"));
    }

    @Test
    void deveRetornarVendedorPorId() throws Exception {
        VendedorDTO vendedor = new VendedorDTO(1L, "Carlos", "Vendas", "carlos@gmail.com");

        when(vendedorService.getById(1L)).thenReturn(vendedor);

        mockMvc.perform(get("/api/vendedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Carlos"))
                .andExpect(jsonPath("$.setor").value("Vendas"))
                .andExpect(jsonPath("$.email").value("carlos@gmail.com"));
    }
}
