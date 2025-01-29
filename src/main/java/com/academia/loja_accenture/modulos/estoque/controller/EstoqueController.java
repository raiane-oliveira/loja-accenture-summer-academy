package com.academia.loja_accenture.modulos.estoque.controller;

import com.academia.loja_accenture.modulos.estoque.dto.EstoqueRequestDTO;
import com.academia.loja_accenture.modulos.estoque.dto.EstoqueResponseDTO;
import com.academia.loja_accenture.modulos.estoque.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/estoques")
@Tag(name = "Estoque", description = "Operações para gerenciar o estoque de produtos")
public class EstoqueController {

    private final EstoqueService estoqueService;

    @Operation(
            summary = "Criar novo estoque",
            description = "Cadastra um novo estoque no sistema.",
            tags = {"Estoque"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Estoque criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao criar o estoque")
            }
    )
    @PostMapping
    public ResponseEntity<EstoqueResponseDTO> criarEstoque(@Valid @RequestBody EstoqueRequestDTO dto) {
        EstoqueResponseDTO response = estoqueService.createEstoque(dto);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(
            summary = "Listar todos os estoques",
            description = "Obtém uma lista de todos os estoques cadastrados.",
            tags = {"Estoque"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de estoques obtida com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao obter a lista de estoques")
            }
    )
    @GetMapping
    public ResponseEntity<List<EstoqueResponseDTO>> listarEstoques() {
        List<EstoqueResponseDTO> response = estoqueService.getAllEstoques();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Consultar estoque por ID",
            description = "Obtém um estoque específico pelo seu ID.",
            tags = {"Estoque"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estoque encontrado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Estoque não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EstoqueResponseDTO> obterEstoquePorId(@PathVariable Long id) {
        EstoqueResponseDTO response = estoqueService.getEstoqueById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Alterar a quantidade do estoque",
            description = "Atualiza a quantidade de um item específico no estoque.",
            tags = {"Estoque"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quantidade do estoque alterada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao alterar a quantidade"),
                    @ApiResponse(responseCode = "404", description = "Estoque não encontrado")
            }
    )
    @PatchMapping("/{id}/quantidade")
    public ResponseEntity<EstoqueResponseDTO> alterarQuantidade(
            @PathVariable Long id,
            @RequestParam Long quantidadeAlterada) {
        EstoqueResponseDTO response = estoqueService.alterarQuantidade(id, quantidadeAlterada);
        return ResponseEntity.ok(response);
    }
}
