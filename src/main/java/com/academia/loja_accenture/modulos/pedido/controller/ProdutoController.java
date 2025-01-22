package com.academia.loja_accenture.modulos.pedido.controller;

import com.academia.loja_accenture.core.PaginationParams;
import com.academia.loja_accenture.core.api.responses.ApiResponse;
import com.academia.loja_accenture.modulos.pedido.dto.AtualizarProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.ProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProdutoController {
  private final ProdutoService produtoService;
  
  @GetMapping("/produtos")
  public ResponseEntity<List<ProdutoDTO>> listarProdutos(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "30") int size
  ) {
    PaginationParams paginationParams = new PaginationParams(page, size);
    var produtos = produtoService.listAll(paginationParams);
    return ResponseEntity.ok(produtos);
  }
  
  @GetMapping("/produtos/vendedor/{vendedorId}")
  public ResponseEntity<List<ProdutoDTO>> listarProdutosPorVendedor(
      @PathVariable Long vendedorId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "30") int size
  ) {
    PaginationParams paginationParams = new PaginationParams(page, size);
    var produtos = produtoService.listByOwner(vendedorId, paginationParams);
    return ResponseEntity.ok(produtos);
  }
  
  @GetMapping("/produtos/{produtoId}")
  public ResponseEntity<ProdutoDTO> obterProdutoPorId(@PathVariable Long produtoId) {
    ProdutoDTO produto = produtoService.getById(produtoId);
    return ResponseEntity.ok(produto);
  }
  
  // TODO: privar acesso a endpoint (apenas vendedores)
  @PostMapping(value = "/produtos", consumes = "application/json")
  public ResponseEntity<ApiResponse> cadastrarProduto(@Valid @RequestBody CadastrarProdutoDTO data) {
    produtoService.save(data);
    return ResponseEntity.status(201).body(new ApiResponse("Produto cadastrado com sucesso"));
  }
  
  // TODO: privar acesso a endpoint (apenas vendedores)
  @DeleteMapping("/produtos/{vendedorId}/{produtoId}")
  public ResponseEntity<ApiResponse> deletarProduto(
      @PathVariable("vendedorId") Long vendedorId,
      @PathVariable("produtoId") Long produtoId
  ) {
    produtoService.delete(vendedorId, produtoId);
    return ResponseEntity.ok(new ApiResponse("Produto removido com sucesso"));
  }
  
  // TODO: privar acesso a endpoint (apenas vendedores)
  @PutMapping("/produtos/{vendedorId}/{produtoId}")
  public ResponseEntity<Void> editarProduto(
      @PathVariable("vendedorId") Long vendedorId,
      @PathVariable("produtoId") Long produtoId,
      @Valid @RequestBody AtualizarProdutoDTO data
  ) {
    produtoService.update(vendedorId, produtoId, data);
    return ResponseEntity.status(204).build();
  }
}
