package com.academia.loja_accenture.modulos.pedido;

import com.academia.loja_accenture.factory.MakeCliente;
import com.academia.loja_accenture.factory.MakeProduto;
import com.academia.loja_accenture.factory.MakeVendedor;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarPedidoDTO;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@Transactional
class PedidoControllerIntegrationTest {
  
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
  
  @BeforeEach
  void setUp() {
    pedidoRepository.deleteAll();
    produtoRepository.deleteAll();
    clienteRepository.deleteAll();
    vendedorRepository.deleteAll();
  }
  
  @Test
  void shouldCreatePedidoSuccessfully() throws Exception {
    Cliente cliente = MakeCliente.create();
    cliente.setNome("Cliente Teste");
    cliente = clienteRepository.save(cliente);
    
    Vendedor vendedor = MakeVendedor.create();
    vendedor.setNome("Vendedor Teste");
    vendedor = vendedorRepository.save(vendedor);
    
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
    
    CadastrarPedidoDTO data = new CadastrarPedidoDTO(
        cliente.getId(),
        vendedor.getId(),
        List.of(produto1.getId(), produto2.getId()),
        "Pedido Teste"
    );
    
    mockMvc.perform(post("/pedidos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(data)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("Pedido cadastrado com sucesso"));
    
    List<Pedido> pedidos = pedidoRepository.findAll();
    assertEquals(1, pedidos.size());
    Pedido pedido = pedidos.getFirst();
    assertEquals("Pedido Teste", pedido.getDescricao());
    assertEquals(2, pedido.getProdutos().size());
    assertEquals(BigDecimal.valueOf(80.00), pedido.getValor());
  }
  
  @Test
  void shouldReturnPedidoById() throws Exception {
    Cliente cliente = MakeCliente.create();
    cliente.setNome("Cliente Teste");
    cliente = clienteRepository.save(cliente);
    
    Vendedor vendedor = MakeVendedor.create();
    vendedor.setNome("Vendedor Teste");
    vendedor = vendedorRepository.save(vendedor);
    
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
    pedido.setProdutos(List.of(produto1, produto2));
    pedido.setValor(BigDecimal.valueOf(80.00));
    pedido.setQuantidade(2);
    pedido = pedidoRepository.save(pedido);
    
    mockMvc.perform(get("/pedidos/{id}", pedido.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(pedido.getId()))
        .andExpect(jsonPath("$.descricao").value("Pedido Teste"))
        .andExpect(jsonPath("$.valor").value(80.00))
        .andExpect(jsonPath("$.quantidade").value(2))
        .andExpect(jsonPath("$.clienteId").value(cliente.getId())) .andExpect(jsonPath("$.vendedorId").value(vendedor.getId())) .andExpect(jsonPath("$.produtos", hasSize(2))); } @Test
  void shouldReturn404WhenPedidoNotFound() throws Exception {
    mockMvc.perform(get("/pedidos/{id}", 999L)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}