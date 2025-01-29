package com.academia.loja_accenture.modulos.rastreamento;

import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.rastreamento.domain.StatusEnum;
import com.academia.loja_accenture.modulos.rastreamento.domain.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StatusPedidoTest {

    private StatusPedido statusPedido;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId(1L);

        statusPedido = new StatusPedido();
        statusPedido.setId(1L);
        statusPedido.setStatus(StatusEnum.PROCESSANDO);
        statusPedido.setCreatedAt(LocalDateTime.now());
        statusPedido.setPedido(pedido);
    }

    @Test
    void testStatusPedidoAttributes() {
        assertEquals(1L, statusPedido.getId());
        assertEquals(StatusEnum.PROCESSANDO, statusPedido.getStatus());
        assertNotNull(statusPedido.getCreatedAt());
        assertEquals(1L, statusPedido.getPedido().getId());
    }

    @Test
    void testSetStatusEnum() {
        statusPedido.setStatus(StatusEnum.ENTREGUE);
        assertEquals(StatusEnum.ENTREGUE, statusPedido.getStatus());
    }
}
