package com.academia.loja_accenture.modulos.estoque.controller;

import com.academia.loja_accenture.modulos.estoque.dto.EstoqueRequestDTO;
import com.academia.loja_accenture.modulos.estoque.dto.EstoqueResponseDTO;
import com.academia.loja_accenture.modulos.estoque.service.EstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;

    // Criar novo estoque
    @PostMapping
    public ResponseEntity<EstoqueResponseDTO> criarEstoque(@Valid @RequestBody EstoqueRequestDTO dto) {
        EstoqueResponseDTO response = estoqueService.createEstoque(dto);
        return ResponseEntity.status(201).body(response);
    }

    // Listar todos os estoques
    @GetMapping
    public ResponseEntity<List<EstoqueResponseDTO>> listarEstoques() {
        List<EstoqueResponseDTO> response = estoqueService.getAllEstoques();
        return ResponseEntity.ok(response);
    }

    // Consultar estoque por ID
    @GetMapping("/{id}")
    public ResponseEntity<EstoqueResponseDTO> obterEstoquePorId(@PathVariable Long id) {
        EstoqueResponseDTO response = estoqueService.getEstoqueById(id);
        return ResponseEntity.ok(response);
    }

    // Alterar a quantidade do estoque
    @PatchMapping("/{id}/quantidade")
    public ResponseEntity<EstoqueResponseDTO> alterarQuantidade(
            @PathVariable Long id,
            @RequestParam Long quantidadeAlterada) {
        EstoqueResponseDTO response = estoqueService.alterarQuantidade(id, quantidadeAlterada);
        return ResponseEntity.ok(response);
    }
}
