package com.academia.loja_accenture.modulos.estoque.service;

import com.academia.loja_accenture.core.exceptions.EstoqueNotFoundException;
import com.academia.loja_accenture.core.exceptions.ProdutoNotFoundException;
import com.academia.loja_accenture.modulos.estoque.domain.Estoque;
import com.academia.loja_accenture.modulos.pedido.dto.ProdutoComQuantidadeDTO;
import com.academia.loja_accenture.modulos.estoque.dto.EstoqueRequestDTO;
import com.academia.loja_accenture.modulos.estoque.dto.EstoqueResponseDTO;
import com.academia.loja_accenture.modulos.estoque.repository.EstoqueRepository;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    // Criar novo estoque
    public EstoqueResponseDTO createEstoque(EstoqueRequestDTO dto) {
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(ProdutoNotFoundException::new);

        Estoque estoque = new Estoque();
        estoque.setProduto(produto);
        estoque.setQuantidade(dto.getQuantidade());
        estoque.setName(produto.getNome() + " - Estoque");

        Estoque savedEstoque = estoqueRepository.save(estoque);
        return mapToResponseDTO(savedEstoque);
    }

    // Alterar a quantidade do estoque
    public EstoqueResponseDTO alterarQuantidade(Long estoqueId, Long quantidadeAlterada) {
        Estoque estoque = estoqueRepository.findById(estoqueId)
                .orElseThrow(EstoqueNotFoundException::new);

        estoque.atualizarQuantidade(quantidadeAlterada);
        Estoque updatedEstoque = estoqueRepository.save(estoque);
        return mapToResponseDTO(updatedEstoque);
    }
    
    public void diminuirQuantidadePorProdutosId(List<ProdutoComQuantidadeDTO> data) {
        data.forEach((produto) -> {
            Estoque estoque = estoqueRepository.findByProdutoId(produto.id()).orElseThrow(ProdutoNotFoundException::new);
            
            // Converte quantidade para negativo
            estoque.atualizarQuantidade((long) produto.quantidade() * -1);
            estoqueRepository.save(estoque);
        });
    }
    
    // Obter estoque por ID
    public EstoqueResponseDTO getEstoqueById(Long id) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(EstoqueNotFoundException::new);
        return mapToResponseDTO(estoque);
    }

    // Listar todos os estoques
    public List<EstoqueResponseDTO> getAllEstoques() {
        return estoqueRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Método para mapear entidade para DTO de resposta
    private EstoqueResponseDTO mapToResponseDTO(Estoque estoque) {
        EstoqueResponseDTO dto = new EstoqueResponseDTO();
        dto.setId(estoque.getId());
        dto.setName(estoque.getName());
        dto.setQuantidade(estoque.getQuantidade());
        dto.setProdutoNome(estoque.getProduto().getNome());
        return dto;
    }
}
