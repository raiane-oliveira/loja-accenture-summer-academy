package com.academia.loja_accenture.modulos.rastreamento.controller;

import com.academia.loja_accenture.modulos.rastreamento.domain.StatusPedido;
import com.academia.loja_accenture.modulos.rastreamento.service.StatusPedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status-pedidos")
@RequiredArgsConstructor
@Tag(name = "Status dos Pedidos", description = "Gerenciar o status dos pedidos")
public class StatusPedidoController {
    private final StatusPedidoService statusPedidoService;

    @Operation(summary = "Criar novo status")
    @PostMapping
    public ResponseEntity<StatusPedido> criarStatus(@RequestBody StatusPedido statusPedido) {
        return ResponseEntity.ok(statusPedidoService.salvar(statusPedido));
    }

    @Operation(summary = "Listar todos os status")
    @GetMapping
    public ResponseEntity<List<StatusPedido>> listarStatus() {
        return ResponseEntity.ok(statusPedidoService.listarTodos());
    }

    @Operation(summary = "Buscar status por ID")
    @GetMapping("/{id}")
    public ResponseEntity<StatusPedido> buscarStatusPorId(@PathVariable Long id) {
        return ResponseEntity.ok(statusPedidoService.buscarPorId(id));
    }
}