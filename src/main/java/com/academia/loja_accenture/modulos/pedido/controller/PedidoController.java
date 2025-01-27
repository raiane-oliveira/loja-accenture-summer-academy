package com.academia.loja_accenture.modulos.pedido.controller;

import com.academia.loja_accenture.core.api.responses.HttpApiResponse;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarPedidoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.PedidoDTO;
import com.academia.loja_accenture.modulos.pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
@Tag(name = "Pedido")
public class PedidoController {
    private final PedidoService pedidoService;
    
    @Operation(
        summary = "Cadastra um novo pedido",
        description = "Realiza o cadastro de um novo pedido no sistema",
        tags = {
            "Pedido"
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao realizar o cadastro")
        }
    )
    @PostMapping
    public ResponseEntity<HttpApiResponse> cadastrarPedido(@Valid @RequestBody CadastrarPedidoDTO data) {
        pedidoService.save(data);
        return ResponseEntity.status(201).body(new HttpApiResponse("Pedido cadastrado com sucesso"));
    }
    
    @Operation(
        summary = "Obter pedido por ID",
        description = "Obtêm um pedido pelo seu ID",
        tags = {
            "Pedido"
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Get realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao realizar o Get")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obterPedido(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.getById(id);
        return ResponseEntity.ok(pedido);
    }
}

