package com.academia.loja_accenture.modulos.estoque;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.academia.loja_accenture.config.RabbitMQMockConfig;
import com.academia.loja_accenture.config.security.TokenService;
import com.academia.loja_accenture.factory.MakeVendedor;
import com.academia.loja_accenture.modulos.usuario.domain.User;
import com.academia.loja_accenture.modulos.usuario.domain.UserRole;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.academia.loja_accenture.modulos.estoque.domain.Estoque;
import com.academia.loja_accenture.modulos.estoque.dto.EstoqueRequestDTO;
import com.academia.loja_accenture.modulos.estoque.repository.EstoqueRepository;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@Transactional
@Import(RabbitMQMockConfig.class)
class EstoqueControllerIT {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private EstoqueRepository estoqueRepository;

  @Autowired
  private ProdutoRepository produtoRepository;
  
  @Autowired
  private VendedorRepository vendedorRepository;
  
  @Autowired
  private TokenService tokenService;
  
  private Vendedor vendedor;
  private String token;
  
  @BeforeEach
  void setUp() {
    estoqueRepository.deleteAll();
    produtoRepository.deleteAll();
    vendedorRepository.deleteAll();
    
    vendedor = MakeVendedor.create();
    vendedor = vendedorRepository.save(vendedor);
    token = tokenService.generateToken(new User(vendedor.getId(), vendedor.getEmail(), vendedor.getSenha(), UserRole.VENDEDOR));
  }

  @Test
  void shouldCreateEstoque() throws Exception {
    Produto produto = new Produto();
    produto.setNome("Produto Teste");
    produto.setDescricao("Descrição Teste");
    produto.setValor(BigDecimal.TEN);
    produto.setVendedor(vendedor);
    produto = produtoRepository.save(produto);

    EstoqueRequestDTO request = new EstoqueRequestDTO(produto.getId(), 50L);

    mockMvc.perform(post("/estoques")
                .header("Authorization", "Bearer " + token) // Adiciona o token no cabeçalho
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Produto Teste - Estoque"))
        .andExpect(jsonPath("$.quantidade").value(50));
  }

  @Test
  void shouldGetAllEstoques() throws Exception {
    Produto produto = new Produto();
    produto.setNome("Produto Teste");
    produto.setDescricao("Descrição Teste");
    produto.setValor(BigDecimal.TEN);
    produto.setVendedor(vendedor);
    produto = produtoRepository.save(produto);

    Estoque estoque = new Estoque();
    estoque.setProduto(produto);
    estoque.setQuantidade(50L);
    estoque.setName(produto.getNome() + " - Estoque");
    estoqueRepository.save(estoque);

    mockMvc.perform(get("/estoques")
            .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Produto Teste - Estoque"))
        .andExpect(jsonPath("$[0].quantidade").value(50));
  }

  @Test
  void shouldGetEstoqueById() throws Exception {
    Produto produto = new Produto();
    produto.setNome("Produto Teste");
    produto.setDescricao("Descrição Teste");
    produto.setValor(BigDecimal.TEN);
    produto.setVendedor(vendedor);
    produto = produtoRepository.save(produto);

    Estoque estoque = new Estoque();
    estoque.setProduto(produto);
    estoque.setQuantidade(50L);
    estoque.setName(produto.getNome() + " - Estoque");
    estoque = estoqueRepository.save(estoque);

    mockMvc.perform(get("/estoques/{id}", estoque.getId())
            .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Produto Teste - Estoque"))
        .andExpect(jsonPath("$.quantidade").value(50));
  }
}
