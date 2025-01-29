package com.academia.loja_accenture.modulos.rastreamento.controller;

import com.academia.loja_accenture.modulos.rastreamento.dto.RegistrarStatusRequestDTO;
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
@Tag(name = "Status do Pedido", description = "Gerenciamento do registro de status dos pedidos.")
public class RegistrarStatusController {

    private final StatusPedidoService statusPedidoService;

    @Operation(summary = "Registrar status do pedido", description = "Vincula um novo status ao pedido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status registrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição!")
    })
    @PostMapping("/{pedidoId}/registrar")
    public ResponseEntity<RegistrarStatusResponseDTO> registrarStatus(
            @PathVariable Long pedidoId,
            @RequestBody RegistrarStatusRequestDTO request) {
        return ResponseEntity.ok(statusPedidoService.registrarStatus(pedidoId, request));
    }
}
