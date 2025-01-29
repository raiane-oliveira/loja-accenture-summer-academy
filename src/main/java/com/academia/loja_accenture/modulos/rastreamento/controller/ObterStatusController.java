package com.academia.loja_accenture.modulos.rastreamento.controller;

import com.academia.loja_accenture.modulos.rastreamento.dto.RegistrarStatusResponseDTO;
import com.academia.loja_accenture.modulos.rastreamento.service.StatusPedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/status-pedidos")
@RequiredArgsConstructor
@Tag(name = "Status do Pedido", description = "Gerenciamento do status atual do pedido.")
public class ObterStatusController {

    private final StatusPedidoService statusPedidoService;

    @Operation(summary = "Obter o status atual do pedido", description = "Retorna o status mais recente do pedido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atual retornado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado!")
    })
    @GetMapping("/{pedidoId}")
    public ResponseEntity<RegistrarStatusResponseDTO> obterStatusAtual(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(statusPedidoService.obterStatusAtual(pedidoId));
    }
}
