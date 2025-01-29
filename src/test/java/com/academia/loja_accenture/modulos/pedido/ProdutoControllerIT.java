package com.academia.loja_accenture.modulos.pedido;

import com.academia.loja_accenture.config.security.TokenService;
import com.academia.loja_accenture.factory.MakeProduto;
import com.academia.loja_accenture.factory.MakeVendedor;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.dto.AtualizarProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import com.academia.loja_accenture.modulos.usuario.domain.User;
import com.academia.loja_accenture.modulos.usuario.domain.UserRole;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@Transactional
class ProdutoControllerIT {
  
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private ProdutoRepository produtoRepository;
  
  @Autowired
  private VendedorRepository vendedorRepository;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private TokenService tokenService;
  
  private String token;
  private Vendedor vendedor;
  
  @BeforeEach
  void setUp() {
    // Limpa o banco de dados antes de cada teste
    produtoRepository.deleteAll();
    vendedorRepository.deleteAll();
    
    // Adiciona um vendedor para os testes
    vendedor = MakeVendedor.create();
    vendedor = vendedorRepository.save(vendedor);
    token = tokenService.generateToken(new User(vendedor.getId(), vendedor.getEmail(), vendedor.getSenha(), UserRole.VENDEDOR));
  }
  
  @Test
  void shouldListAllProducts() throws Exception {
    // Adiciona produtos para os testes
    var produto1 = MakeProduto.create(null, "Produto 1", "Descrição 1", BigDecimal.TEN, vendedor);
    var produto2 = MakeProduto.create(null, "Produto 2", "Descrição 2", BigDecimal.valueOf(20), vendedor);
    produtoRepository.saveAll(List.of(produto1, produto2));
    
    mockMvc.perform(get("/produtos")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }
  
  @Test
  void shouldGetProductById() throws Exception {
    // Adiciona um produto ao banco de dados
    var produto = MakeProduto.create(null, "Produto Teste", "Descrição Teste", BigDecimal.TEN, vendedor);
    produto = produtoRepository.save(produto);
    
    mockMvc.perform(get("/produtos/" + produto.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Produto Teste"));
  }
  
  @Test
  void shouldCreateProduct() throws Exception {
    CadastrarProdutoDTO produtoDTO = new CadastrarProdutoDTO(
        "Novo Produto", "Descrição do Produto", BigDecimal.valueOf(30), vendedor.getId());
    
    mockMvc.perform(post("/produtos")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(produtoDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("Produto cadastrado com sucesso"));
  }
  
  @Test
  void shouldUpdateProduct() throws Exception {
    // Adiciona um produto ao banco de dados
    var produto = MakeProduto.create(null, "Produto Teste", "Descrição Teste", BigDecimal.TEN, vendedor);
    produto = produtoRepository.save(produto);
    vendedor.setProdutos(List.of(produto));
    
    var atualizarProdutoDTO = new AtualizarProdutoDTO("Produto Atualizado", null, BigDecimal.valueOf(50));
    
    mockMvc.perform(put("/produtos/" + vendedor.getId() + "/" + produto.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(atualizarProdutoDTO)))
        .andExpect(status().isNoContent());
    
    Optional<Produto> updatedProduct = produtoRepository.findById(produto.getId());
    assertTrue(updatedProduct.isPresent());
    assertEquals("Produto Atualizado", updatedProduct.get().getNome());
    assertEquals(BigDecimal.valueOf(50), updatedProduct.get().getValor());
  }
  
  @Test
  void shouldDeleteProduct() throws Exception {
    // Adiciona um produto ao banco de dados
    var produto = MakeProduto.create(null, "Produto Teste", "Descrição Teste", BigDecimal.TEN, vendedor);
    produto = produtoRepository.save(produto);
    
    vendedor.getProdutos().add(produto);
    vendedorRepository.save(vendedor);
    
    mockMvc.perform(delete("/produtos/" + vendedor.getId() + "/" + produto.getId())
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Produto removido com sucesso"));
    
    Optional<Produto> deletedProduct = produtoRepository.findById(produto.getId());
    assertFalse(deletedProduct.isPresent());
  }
}