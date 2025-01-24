package com.academia.loja_accenture.modulos.pedido.controller;

import com.academia.loja_accenture.modulos.pedido.dto.CadastrarPedidoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.PedidoDTO;
import com.academia.loja_accenture.modulos.pedido.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoDTO> cadastrarPedido(@Valid @RequestBody CadastrarPedidoDTO data) {
        PedidoDTO pedido = pedidoService.save(data);
        return ResponseEntity.status(201).body(pedido);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obterPedido(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.getById(id);
        return ResponseEntity.ok(pedido);
    }
}

