package com.academia.loja_accenture.modulos.pagamento.controller;

import com.academia.loja_accenture.modulos.pagamento.domain.Pagamento;
import com.academia.loja_accenture.modulos.pagamento.dto.PagamentoDTO;
import com.academia.loja_accenture.modulos.pagamento.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Pagamento")
public class PagamentoController {
  private final PagamentoService pagamentoService;
  
  // TODO: privar apenas para usuários autenticados
  @Operation(
      summary = "Obter pagamento por ID",
      tags = { "Pagamento" },
      responses = {
          @ApiResponse(responseCode = "200", description = "Get realizado com sucesso"),
          @ApiResponse(responseCode = "404", description = "Pagamento não encontrado"),
          @ApiResponse(responseCode = "400", description = "Erro ao realizar o Get")
      }
  )
  @GetMapping("/pagamentos/{pagamentoId}")
  public ResponseEntity<PagamentoDTO> obterPagamento(@PathVariable Long pagamentoId) {
    return ResponseEntity.ok(pagamentoService.getById(pagamentoId));
  }
}
