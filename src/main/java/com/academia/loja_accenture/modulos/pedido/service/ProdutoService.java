package com.academia.loja_accenture.modulos.pedido.service;

import com.academia.loja_accenture.core.PaginationParams;
import com.academia.loja_accenture.core.exceptions.VendedorNotFoundException;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.dto.AtualizarProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.ProdutoDTO;
import com.academia.loja_accenture.core.exceptions.ProdutoNotFoundException;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {
  private final ProdutoRepository produtoRepository;
  private final VendedorRepository vendedorRepository;
  
  public List<ProdutoDTO> listAll(PaginationParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size());
    Page<Produto> produtos = produtoRepository.findAll(pageable);
    
    return produtos.map(this::convertToDTO).toList();
  }
  
  public List<ProdutoDTO> listByOwner(Long ownerId, PaginationParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size());
    Page<Produto> produtos = produtoRepository.findByOwnerId(ownerId, pageable);
    
    return produtos.stream().map(this::convertToDTO).toList();
  }
  
  public ProdutoDTO getById(Long id) {
    Produto produto = produtoRepository.findById(id).orElse(null);
    if (produto == null) {
      throw new ProdutoNotFoundException();
    }
    
    return convertToDTO(produto);
  }
  
  public Produto save(CadastrarProdutoDTO data) {
    Vendedor vendedor = vendedorRepository.findById(data.vendedorId())
          .orElseThrow(VendedorNotFoundException::new);

    Produto produto = new Produto();
    produto.setNome(data.nome());
    produto.setDescricao(data.descricao());
    produto.setValor(data.valor());
    produto.setVendedor(vendedor);
    
    return produtoRepository.save(produto);
  }
  
  public void delete(Long vendedorId, Long produtoId) {
    Vendedor vendedor = vendedorRepository.findById(vendedorId)
          .orElseThrow(VendedorNotFoundException::new);

    Produto produto = produtoRepository.findById(produtoId).orElseThrow(
        ProdutoNotFoundException::new);
    
    boolean successful = vendedor.getProdutos().remove(produto);
    if (!successful) {
      throw new ProdutoNotFoundException();
    }
    
    produtoRepository.delete(produto);
  }
  
  public void update(Long vendedorId, Long produtoId, AtualizarProdutoDTO data) {
    Vendedor vendedor = vendedorRepository.findById(vendedorId)
          .orElseThrow(VendedorNotFoundException::new);

    Produto produto = produtoRepository.findById(produtoId).orElseThrow(
        ProdutoNotFoundException::new);
    
    boolean successful = vendedor.getProdutos().contains(produto);
    if (!successful) {
      throw new ProdutoNotFoundException();
    }
    
    if (data.nome() != null && !data.nome().trim().isEmpty()) {
      produto.setNome(data.nome());
    }
    if (data.descricao() != null && !data.descricao().trim().isEmpty()) {
      produto.setDescricao(data.descricao());
    }
    if (data.valor() != null) {
      produto.setValor(data.valor());
    }
    
    produtoRepository.save(produto);
  }
  
  private ProdutoDTO convertToDTO(Produto produto) {
   return new ProdutoDTO(
       produto.getId(),
       produto.getNome(),
       produto.getDescricao(),
       produto.getValor(),
       produto.getCreatedAt(),
       produto.getVendedor().getId());
  }
}
