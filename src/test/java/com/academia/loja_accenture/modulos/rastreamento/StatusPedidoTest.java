package com.academia.loja_accenture.modulos.rastreamento;

import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.rastreamento.domain.StatusPedido;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatusPedidoTest {

    @Test
    void deveCriarStatusPedidoComSucesso() {
        Long id = 1L;
        String descricao = "Pedido em andamento";
        LocalDateTime createdAt = LocalDateTime.now();
        List<Pedido> pedidos = new ArrayList<>();

        StatusPedido statusPedido = new StatusPedido(id, descricao, createdAt, pedidos);

        assertEquals(id, statusPedido.getId());
        assertEquals(descricao, statusPedido.getDescricao());
        assertEquals(createdAt, statusPedido.getCreatedAt());
        assertEquals(pedidos, statusPedido.getPedidos());
    }

    @Test
    void deveAlterarPropriedadesDoStatusPedido() {
        StatusPedido statusPedido = new StatusPedido();
        statusPedido.setId(1L);
        statusPedido.setDescricao("Pedido entregue");
        statusPedido.setCreatedAt(LocalDateTime.now());
        statusPedido.setPedidos(new ArrayList<>());

        assertNotNull(statusPedido.getId());
        assertEquals("Pedido entregue", statusPedido.getDescricao());
        assertNotNull(statusPedido.getCreatedAt());
        assertNotNull(statusPedido.getPedidos());
    }
}
