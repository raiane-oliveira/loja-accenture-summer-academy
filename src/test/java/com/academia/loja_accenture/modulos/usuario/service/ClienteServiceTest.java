package com.academia.loja_accenture.modulos.usuario.service;

import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.dto.ClienteDTO;
import com.academia.loja_accenture.modulos.usuario.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarTodosOsClientes() {

        Cliente cliente1 = new Cliente(1L, "João", "joao@gmail.com", "senha123");
        Cliente cliente2 = new Cliente(2L, "Maria", "maria@gmail.com", "senha456");

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        List<ClienteDTO> resultado = clienteService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("João", resultado.get(0).nome());
        assertEquals("Maria", resultado.get(1).nome());

        verify(clienteRepository, times(1)).findAll();
    }
}
