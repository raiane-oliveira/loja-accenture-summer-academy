package com.academia.loja_accenture.modulos.estoque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.academia.loja_accenture.config.RabbitMQMockConfig;
import com.academia.loja_accenture.core.exceptions.EstoqueNotFoundException;
import com.academia.loja_accenture.core.exceptions.ProdutoNotFoundException;
import com.academia.loja_accenture.modulos.estoque.domain.Estoque;
import com.academia.loja_accenture.modulos.estoque.dto.EstoqueRequestDTO;
import com.academia.loja_accenture.modulos.estoque.dto.EstoqueResponseDTO;
import com.academia.loja_accenture.modulos.estoque.repository.EstoqueRepository;
import com.academia.loja_accenture.modulos.estoque.service.EstoqueService;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Import(RabbitMQMockConfig.class)
class EstoqueServiceTest {
  
  @Mock
  private EstoqueRepository estoqueRepository;
  
  @Mock
  private ProdutoRepository produtoRepository;
  
  @InjectMocks
  private EstoqueService estoqueService;
  
  @Test
  void shouldCreateEstoqueSuccessfully() {
    EstoqueRequestDTO requestDTO = new EstoqueRequestDTO(1L, 10L);
    Produto produto = new Produto(1L, "Produto Teste", "Descrição", BigDecimal.TEN);
    Estoque estoque = new Estoque(null, produto, 10L, "Produto Teste - Estoque");
    Estoque savedEstoque = new Estoque(1L, produto, 10L, "Produto Teste - Estoque");
    
    when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
    when(estoqueRepository.save(any(Estoque.class))).thenReturn(savedEstoque);
    
    EstoqueResponseDTO response = estoqueService.createEstoque(requestDTO);
    
    assertEquals(1L, response.getId());
    assertEquals("Produto Teste - Estoque", response.getName());
    assertEquals(10L, response.getQuantidade());
    assertEquals("Produto Teste", response.getProdutoNome());
  }
  
  @Test
  void shouldThrowExceptionWhenCreatingEstoqueWithInvalidProduto() {
    EstoqueRequestDTO requestDTO = new EstoqueRequestDTO(1L, 10L);
    
    when(produtoRepository.findById(1L)).thenReturn(Optional.empty());
    
    assertThrows(ProdutoNotFoundException.class, () -> estoqueService.createEstoque(requestDTO));
  }
  
  @Test
  void shouldAlterarQuantidadeSuccessfully() {
    Estoque estoque = new Estoque(1L, new Produto(1L, "Produto", "Desc", BigDecimal.TEN), 10L, "Produto - Estoque");
    when(estoqueRepository.findById(1L)).thenReturn(Optional.of(estoque));
    when(estoqueRepository.save(any(Estoque.class))).thenReturn(estoque);
    
    EstoqueResponseDTO response = estoqueService.alterarQuantidade(1L, 5L);
    
    assertEquals(15L, response.getQuantidade());
  }
  
  @Test
  void shouldThrowExceptionWhenAlteringQuantidadeForInvalidEstoque() {
    when(estoqueRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(EstoqueNotFoundException.class, () -> estoqueService.alterarQuantidade(1L, 5L));
  }
  
  @Test
  void shouldGetEstoqueByIdSuccessfully() {
    Estoque estoque = new Estoque(1L, new Produto(1L, "Produto", "Desc", BigDecimal.TEN), 10L, "Produto - Estoque");
    when(estoqueRepository.findById(1L)).thenReturn(Optional.of(estoque));
    
    EstoqueResponseDTO response = estoqueService.getEstoqueById(1L);
    
    assertEquals(1L, response.getId());
    assertEquals("Produto - Estoque", response.getName());
  }
  
  @Test
  void shouldThrowExceptionWhenGettingInvalidEstoqueById() {
    when(estoqueRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(EstoqueNotFoundException.class, () -> estoqueService.getEstoqueById(1L));
  }
  
  @Test
  void shouldListAllEstoquesSuccessfully() {
    List<Estoque> estoques = List.of(
        new Estoque(1L, new Produto(1L, "Produto1", "Desc1", BigDecimal.TEN), 10L, "Produto1 - Estoque"),
        new Estoque(2L, new Produto(2L, "Produto2", "Desc2", BigDecimal.TEN), 20L, "Produto2 - Estoque")
    );
    when(estoqueRepository.findAll()).thenReturn(estoques);
    
    List<EstoqueResponseDTO> response = estoqueService.getAllEstoques();
    
    assertEquals(2, response.size());
    assertEquals("Produto1 - Estoque", response.get(0).getName());
    assertEquals("Produto2 - Estoque", response.get(1).getName());
  }
}
