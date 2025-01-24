package com.academia.loja_accenture.modulos.pagamento;

import com.academia.loja_accenture.modulos.pagamento.domain.MetodoPagamento;
import com.academia.loja_accenture.modulos.pagamento.domain.Pagamento;
import com.academia.loja_accenture.modulos.pagamento.domain.StatusPagamento;
import com.academia.loja_accenture.modulos.pagamento.repository.PagamentoRepository;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@Transactional
public class PagamentoControllerIntegrationTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private PagamentoRepository pagamentoRepository;
  
  @Autowired
  private PedidoRepository pedidoRepository;
  
  @BeforeEach
  void setUp() {
    pagamentoRepository.deleteAll();
    pedidoRepository.deleteAll();
  }
  
  @Test
  void shouldReturnPagamentoById() throws Exception {
    Pedido pedido = new Pedido();
    pedido.setDescricao("Pedido Teste");
    pedido.setQuantidade(1);
    pedido.setValor(BigDecimal.valueOf(100.00));
    pedido = pedidoRepository.save(pedido);
    
    Pagamento pagamento = new Pagamento();
    pagamento.setPedido(pedido);
    pagamento.setStatus(StatusPagamento.PROCESSANDO);
    pagamento.setMetodo(MetodoPagamento.CREDITO);
    pagamento.setValor(BigDecimal.valueOf(100.00));
    pagamento = pagamentoRepository.save(pagamento);
    
    mockMvc.perform(get("/pagamentos/{pagamentoId}", pagamento.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(pagamento.getId()))
        .andExpect(jsonPath("$.status").value("PROCESSANDO"))
        .andExpect(jsonPath("$.metodo").value("CREDITO"))
        .andExpect(jsonPath("$.valor").value(100.00));
  }
  
  @Test
  void shouldReturnNotFoundWhenPagamentoDoesNotExist() throws Exception {
    mockMvc.perform(get("/pagamentos/{pagamentoId}", 999)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}
