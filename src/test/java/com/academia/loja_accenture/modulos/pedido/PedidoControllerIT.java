package com.academia.loja_accenture.modulos.pedido;

import com.academia.loja_accenture.config.RabbitMQMockConfig;
import com.academia.loja_accenture.config.security.TokenService;
import com.academia.loja_accenture.factory.MakeCliente;
import com.academia.loja_accenture.factory.MakeProduto;
import com.academia.loja_accenture.factory.MakeVendedor;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarPedidoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.ProdutoComQuantidadeDTO;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import com.academia.loja_accenture.modulos.usuario.domain.User;
import com.academia.loja_accenture.modulos.usuario.domain.UserRole;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.repository.ClienteRepository;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@Transactional
@Import(RabbitMQMockConfig.class)
class PedidoControllerIT {
  
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private ProdutoRepository produtoRepository;
  
  @Autowired
  private ClienteRepository clienteRepository;
  
  @Autowired
  private VendedorRepository vendedorRepository;
  
  @Autowired
  private PedidoRepository pedidoRepository;
  
  @Autowired
  private TokenService tokenService;
  
  private Cliente cliente;
  private Vendedor vendedor;
  private String token;
  
  @BeforeEach
  void setUp() {
    pedidoRepository.deleteAll();
    produtoRepository.deleteAll();
    clienteRepository.deleteAll();
    vendedorRepository.deleteAll();
    
    vendedor = vendedorRepository.save(MakeVendedor.create());
    cliente = clienteRepository.save(MakeCliente.create());
    token = tokenService.generateToken(new User(cliente.getId(), cliente.getEmail(), cliente.getSenha(), UserRole.CLIENTE));
  }
  
  @Test
  void shouldCreatePedidoSuccessfully() throws Exception {
    Produto produto1 = MakeProduto.create();
    produto1.setNome("Produto 1");
    produto1.setValor(BigDecimal.valueOf(50.00));
    produto1.setVendedor(vendedor);
    produto1 = produtoRepository.save(produto1);
    
    Produto produto2 = MakeProduto.create();
    produto2.setNome("Produto 2");
    produto2.setValor(BigDecimal.valueOf(30.00));
    produto2.setVendedor(vendedor);
    produto2 = produtoRepository.save(produto2);
    
    List<ProdutoComQuantidadeDTO> produtosComQuant = List.of(
        new ProdutoComQuantidadeDTO(produto1.getId(), 1),
        new ProdutoComQuantidadeDTO(produto2.getId(), 1)
    );
    CadastrarPedidoDTO data = new CadastrarPedidoDTO(
        cliente.getId(),
        vendedor.getId(),
        produtosComQuant,
        "Pedido Teste"
    );
    
    mockMvc.perform(post("/pedidos")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(data)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("Pedido cadastrado com sucesso"));
  }
  
  @Test
  void shouldReturnPedidoById() throws Exception {
    Produto produto1 = MakeProduto.create();
    produto1.setNome("Produto 1");
    produto1.setValor(BigDecimal.valueOf(50.00));
    produto1.setVendedor(vendedor);
    produto1 = produtoRepository.save(produto1);
    
    Produto produto2 = MakeProduto.create();
    produto2.setNome("Produto 2");
    produto2.setValor(BigDecimal.valueOf(30.00));
    produto2.setVendedor(vendedor);
    produto2 = produtoRepository.save(produto2);
    
    Pedido pedido = new Pedido();
    pedido.setDescricao("Pedido Teste");
    pedido.setCliente(cliente);
    pedido.setVendedor(vendedor);
    pedido.addProduto(produto1, 1);
    pedido.addProduto(produto2, 1);
    pedido.setValor(BigDecimal.valueOf(80.00));
    pedido.setQuantidade(2);
    pedido = pedidoRepository.save(pedido);
    
    mockMvc.perform(get("/pedidos/{id}", pedido.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(pedido.getId()))
        .andExpect(jsonPath("$.descricao").value("Pedido Teste"))
        .andExpect(jsonPath("$.valor").value(80.00))
        .andExpect(jsonPath("$.quantidade").value(2))
        .andExpect(jsonPath("$.clienteId").value(cliente.getId())) .andExpect(jsonPath("$.vendedorId").value(vendedor.getId())) .andExpect(jsonPath("$.produtos", hasSize(2))); } @Test
  
  void shouldReturn400WhenPedidoNotFound() throws Exception {
    mockMvc.perform(get("/pedidos/{id}", 999L)
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}