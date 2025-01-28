package com.academia.loja_accenture.modulos.pedido.controller;

import com.academia.loja_accenture.core.PaginationParams;
import com.academia.loja_accenture.core.api.responses.HttpApiResponse;
import com.academia.loja_accenture.modulos.pedido.dto.AtualizarProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.ProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.service.ProdutoService;
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
@Tag(name = "Produto")
public class ProdutoController {
  private final ProdutoService produtoService;
  
  @Operation(
      summary = "Lista todos os produtos",
      description = "Lista todos os produtos da loja por paginação",
      tags = {
          "Produto"
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "Listagem de produtos realizada com sucesso"),
          @ApiResponse(responseCode = "400", description = "Erro ao realizar a listagem de produtos")
      }
  )
  @GetMapping("/produtos")
  public ResponseEntity<List<ProdutoDTO>> listarProdutos(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "30") int size
  ) {
    PaginationParams paginationParams = new PaginationParams(page, size);
    var produtos = produtoService.listAll(paginationParams);
    return ResponseEntity.ok(produtos);
  }
  
  @Operation(
      summary = "Lista produtos por vendedor",
      description = "Lista todos os produtos de um vendedor por paginação",
      tags = {
          "Produto"
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "Listagem de produtos realizada com sucesso"),
          @ApiResponse(responseCode = "400", description = "Erro ao realizar a listagem de produtos")
      }
  )
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
  
  @Operation(
      summary = "Obter produto por ID",
      description = "Obtêm um produto pelo seu ID",
      tags = {
          "Produto"
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "Get realizado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Erro ao realizar o Get")
      }
  )
  @GetMapping("/produtos/{produtoId}")
  public ResponseEntity<ProdutoDTO> obterProdutoPorId(@PathVariable Long produtoId) {
    ProdutoDTO produto = produtoService.getById(produtoId);
    return ResponseEntity.ok(produto);
  }
  
  @Operation(
      summary = "Cadastra um novo produto",
      description = "Realiza o cadastro de um novo produto no sistema",
      tags = {
          "Produto"
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Erro ao realizar o cadastro")
      }
  )
  @PostMapping(value = "/produtos", consumes = "application/json")
  public ResponseEntity<HttpApiResponse> cadastrarProduto(@RequestBody @Valid CadastrarProdutoDTO data) {
    produtoService.save(data);
    return ResponseEntity.status(201).body(new HttpApiResponse("Produto cadastrado com sucesso"));
  }
  
  @Operation(
      summary = "Deleta o produto de um vendedor",
      description = "Exclui o produto de um vendedor existente",
      tags = {
          "Produto"
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "Remoção realizada com sucesso"),
          @ApiResponse(responseCode = "400", description = "Erro ao realizar a remoção")
      }
  )
  @DeleteMapping("/produtos/{vendedorId}/{produtoId}")
  public ResponseEntity<HttpApiResponse> deletarProduto(
      @PathVariable("vendedorId") Long vendedorId,
      @PathVariable("produtoId") Long produtoId
  ) {
    produtoService.delete(vendedorId, produtoId);
    return ResponseEntity.ok(new HttpApiResponse("Produto removido com sucesso"));
  }
  
  @Operation(
      summary = "Atualiza o produto de um vendedor",
      description = "Atualiza o produto de um vendedor existente",
      tags = {
          "Produto"
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso"),
          @ApiResponse(responseCode = "400", description = "Erro ao realizar a atualização")
      }
  )
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
