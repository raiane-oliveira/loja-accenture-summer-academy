package com.academia.loja_accenture.modulos.pedido.controller;

import com.academia.loja_accenture.core.api.responses.ApiResponse;
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
    public ResponseEntity<ApiResponse> cadastrarPedido(@Valid @RequestBody CadastrarPedidoDTO data) {
        pedidoService.save(data);
        return ResponseEntity.status(201).body(new ApiResponse("Pedido cadastrado com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obterPedido(@PathVariable Long id) {
        try {
            PedidoDTO pedido = pedidoService.getById(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

