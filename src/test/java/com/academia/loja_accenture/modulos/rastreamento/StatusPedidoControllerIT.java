package com.academia.loja_accenture.modulos.rastreamento;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.academia.loja_accenture.config.security.TokenService;
import com.academia.loja_accenture.core.exceptions.PedidoNotFoundException;
import com.academia.loja_accenture.factory.MakeCliente;
import com.academia.loja_accenture.factory.MakeVendedor;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import com.academia.loja_accenture.modulos.rastreamento.domain.StatusEnum;
import com.academia.loja_accenture.modulos.rastreamento.domain.StatusPedido;
import com.academia.loja_accenture.modulos.rastreamento.dto.RegistrarStatusRequestDTO;
import com.academia.loja_accenture.modulos.rastreamento.repository.StatusPedidoRepository;
import com.academia.loja_accenture.modulos.rastreamento.service.StatusPedidoService;
import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.domain.User;
import com.academia.loja_accenture.modulos.usuario.domain.UserRole;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.repository.ClienteRepository;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@Transactional
public class StatusPedidoControllerIT {
  
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Mock
  private StatusPedidoService statusPedidoService;
  
  @Autowired
  private TokenService tokenService;
  
  @Autowired
  private VendedorRepository vendedorRepository;
  
  @Autowired
  private PedidoRepository pedidoRepository;
  
  @Autowired
  private StatusPedidoRepository statusPedidoRepository;
  
  @Autowired
  private ClienteRepository clienteRepository;
  
  private Vendedor vendedor;
  private Cliente cliente;
  private String token;
  
  @BeforeEach
  void setUp() {
    statusPedidoRepository.deleteAll();
    pedidoRepository.deleteAll();
    clienteRepository.deleteAll();
    vendedorRepository.deleteAll();
    
    vendedor = vendedorRepository.save(MakeVendedor.create());
    cliente = clienteRepository.save(MakeCliente.create());
    token = tokenService.generateToken(new User(vendedor.getId(), vendedor.getEmail(), vendedor.getSenha(), UserRole.VENDEDOR));
  }
  
  @Test
  void registrarStatus_validRequest_shouldReturnOk() throws Exception {
    Pedido pedido = new Pedido();
    pedido.setDescricao("teste");
    pedido.setValor(BigDecimal.TEN);
    pedido.setQuantidade(1);
    pedido.setVendedor(vendedor);
    pedido.setCliente(cliente);
    pedido = pedidoRepository.saveAndFlush(pedido);
    Long pedidoId = pedido.getId();
    
    RegistrarStatusRequestDTO request = new RegistrarStatusRequestDTO(StatusEnum.PROCESSANDO);
    
    mockMvc.perform(post("/api/status-pedidos/{pedidoId}/registrar", pedidoId)
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("PROCESSANDO"));
  }
  
  @Test
  void obterStatusAtual_validPedidoId_shouldReturnOk() throws Exception {
    Pedido pedido = new Pedido();
    pedido.setDescricao("teste");
    pedido.setValor(BigDecimal.TEN);
    pedido.setQuantidade(1);
    pedido.setVendedor(vendedor);
    pedido.setCliente(cliente);
    pedido = pedidoRepository.saveAndFlush(pedido);
    Long pedidoId = pedido.getId();
    
    statusPedidoRepository.save(new StatusPedido(StatusEnum.ENTREGUE, pedido));
    
    mockMvc.perform(get("/api/status-pedidos/{pedidoId}", pedidoId)
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("ENTREGUE"));
  }
  
  @Test
  void obterHistorico_validPedidoId_shouldReturnOk() throws Exception {
    Pedido pedido = new Pedido();
    pedido.setDescricao("teste");
    pedido.setValor(BigDecimal.TEN);
    pedido.setQuantidade(1);
    pedido.setVendedor(vendedor);
    pedido.setCliente(cliente);
    pedido = pedidoRepository.saveAndFlush(pedido);
    Long pedidoId = pedido.getId();
    
    statusPedidoRepository.save(new StatusPedido(StatusEnum.PROCESSANDO, pedido));
    statusPedidoRepository.save(new StatusPedido(StatusEnum.ENTREGUE, pedido));
    
    mockMvc.perform(get("/api/status-pedidos/{pedidoId}/historico", pedidoId)
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].status").value("PROCESSANDO"))
        .andExpect(jsonPath("$[1].status").value("ENTREGUE"));
  }
  
  @Test
  void registrarStatus_pedidoNotFound_shouldReturnError() throws Exception {
    Long pedidoId = 999L;
    RegistrarStatusRequestDTO request = new RegistrarStatusRequestDTO();
    request.setStatus(StatusEnum.PROCESSANDO);
    
    when(statusPedidoService.registrarStatus(pedidoId, request))
        .thenThrow(new PedidoNotFoundException());
    
    mockMvc.perform(post("/api/status-pedidos/{pedidoId}/registrar", pedidoId)
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  void obterStatusAtual_pedidoNotFound_shouldReturnError() throws Exception {
    Long pedidoId = 999L;
    
    when(statusPedidoService.obterStatusAtual(pedidoId))
        .thenThrow(new PedidoNotFoundException());
    
    mockMvc.perform(get("/api/status-pedidos/{pedidoId}", pedidoId)
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  void obterHistorico_pedidoNotFound_shouldReturnError() throws Exception {
    Long pedidoId = 999L;
    
    when(statusPedidoService.obterHistorico(pedidoId))
        .thenThrow(new PedidoNotFoundException());
    
    mockMvc.perform(get("/api/status-pedidos/{pedidoId}/historico", pedidoId)
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isBadRequest());
  }
}