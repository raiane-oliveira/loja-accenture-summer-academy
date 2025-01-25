package com.academia.loja_accenture.modulos.rastreamento.service;

import com.academia.loja_accenture.modulos.rastreamento.domain.StatusPedido;
import com.academia.loja_accenture.modulos.rastreamento.repository.StatusPedidoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatusPedidoServiceTest {

    @InjectMocks
    private StatusPedidoService statusPedidoService;

    @Mock
    private StatusPedidoRepository statusPedidoRepository;

    public StatusPedidoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarStatusComSucesso() {
        StatusPedido statusPedido = new StatusPedido(null, "Enviado", null, null);
        StatusPedido statusSalvo = new StatusPedido(1L, "Enviado", null, null);

        when(statusPedidoRepository.save(any(StatusPedido.class))).thenReturn(statusSalvo);

        StatusPedido resultado = statusPedidoService.salvar(statusPedido);

        assertNotNull(resultado);
        assertEquals("Enviado", resultado.getDescricao());
        verify(statusPedidoRepository, times(1)).save(any(StatusPedido.class));
    }
}
