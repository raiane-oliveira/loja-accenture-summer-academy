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

import java.util.List;

@RestController
@RequestMapping("/api/status-pedidos")
@RequiredArgsConstructor
@Tag(name = "Status do Pedido", description = "Gerenciamento do histórico de status dos pedidos.")
public class ObterHistoricoController {

    private final StatusPedidoService statusPedidoService;

    @Operation(summary = "Obter histórico do status", description = "Retorna o histórico de status associados ao pedido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado!")
    })
    @GetMapping("/{pedidoId}/historico")
    public ResponseEntity<List<RegistrarStatusResponseDTO>> obterHistorico(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(statusPedidoService.obterHistorico(pedidoId));
    }
}
