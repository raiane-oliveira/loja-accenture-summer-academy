package com.academia.loja_accenture.modulos.pagamento;

import com.academia.loja_accenture.config.RabbitMQMockConfig;
import com.academia.loja_accenture.config.security.TokenService;
import com.academia.loja_accenture.factory.*;
import com.academia.loja_accenture.modulos.pagamento.domain.MetodoPagamento;
import com.academia.loja_accenture.modulos.pagamento.domain.Pagamento;
import com.academia.loja_accenture.modulos.pagamento.domain.StatusPagamento;
import com.academia.loja_accenture.modulos.pagamento.repository.PagamentoRepository;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.domain.User;
import com.academia.loja_accenture.modulos.usuario.domain.UserRole;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.repository.ClienteRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@Transactional
@Import(RabbitMQMockConfig.class)
public class PagamentoControllerIT {
  
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private PagamentoRepository pagamentoRepository;
  
  @Autowired
  private PedidoRepository pedidoRepository;
  
  @Autowired
  private VendedorRepository vendedorRepository;
  
  @Autowired
  private TokenService tokenService;
  
  @Autowired
  private ClienteRepository clienteRepository;
  
  private Cliente cliente;
  private Vendedor vendedor;
  private String token;
  
  @BeforeEach
  void setUp() {
    pagamentoRepository.deleteAll();
    pedidoRepository.deleteAll();
    clienteRepository.deleteAll();
    vendedorRepository.deleteAll();
    
    vendedor = vendedorRepository.save(MakeVendedor.create());
    cliente = clienteRepository.save(MakeCliente.create());
    token = tokenService.generateToken(new User(vendedor.getId(), vendedor.getEmail(), vendedor.getSenha(), UserRole.VENDEDOR));
  }
  
  @Test
  void shouldReturnPagamentoById() throws Exception {
    Pedido pedido = new Pedido();
    pedido.setDescricao("teste");
    pedido.setValor(BigDecimal.TEN);
    pedido.setQuantidade(1);
    pedido.setVendedor(vendedor);
    pedido.setCliente(cliente);
    pedido = pedidoRepository.saveAndFlush(pedido);
    
    Pagamento pagamento = new Pagamento();
    pagamento.setPedido(pedido);
    pagamento.setStatus(StatusPagamento.PROCESSANDO);
    pagamento.setMetodo(MetodoPagamento.CREDITO);
    pagamento.setValor(BigDecimal.valueOf(100.00));
    pagamento = pagamentoRepository.save(pagamento);
    
    mockMvc.perform(get("/pagamentos/{pagamentoId}", pagamento.getId())
            .header("Authorization", "Bearer " + token) // Adiciona o token no cabeçalho
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(pagamento.getId()))
        .andExpect(jsonPath("$.status").value("PROCESSANDO"))
        .andExpect(jsonPath("$.metodo").value("CREDITO"))
        .andExpect(jsonPath("$.valor").value(100.00));
  }
  
  @Test
  void shouldReturnErrorWhenPagamentoDoesNotExist() throws Exception {
    mockMvc.perform(get("/pagamentos/{pagamentoId}", 999)
            .header("Authorization", "Bearer " + token) // Adiciona o token no cabeçalho
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
