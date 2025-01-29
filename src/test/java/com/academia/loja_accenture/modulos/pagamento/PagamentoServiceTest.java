package com.academia.loja_accenture.modulos.pagamento;

import com.academia.loja_accenture.config.RabbitMQMockConfig;
import com.academia.loja_accenture.core.exceptions.PagamentoNotFoundException;
import com.academia.loja_accenture.core.exceptions.PedidoNotFoundException;
import com.academia.loja_accenture.modulos.pagamento.domain.MetodoPagamento;
import com.academia.loja_accenture.modulos.pagamento.domain.Pagamento;
import com.academia.loja_accenture.modulos.pagamento.domain.StatusPagamento;
import com.academia.loja_accenture.modulos.pagamento.dto.AtualizarPagamentoDTO;
import com.academia.loja_accenture.modulos.pagamento.dto.PagamentoDTO;
import com.academia.loja_accenture.modulos.pagamento.dto.RegistrarPagamentoDTO;
import com.academia.loja_accenture.modulos.pagamento.repository.PagamentoRepository;
import com.academia.loja_accenture.modulos.pagamento.service.PagamentoService;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(RabbitMQMockConfig.class)
class PagamentoServiceTest {
  
  @InjectMocks
  private PagamentoService pagamentoService;
  
  @Mock
  private PagamentoRepository pagamentoRepository;
  
  @Mock
  private PedidoRepository pedidoRepository;
  
  @Test
  void shouldSavePagamentoSuccessfully() {
    Long pedidoId = 1L;
    Pedido pedido = new Pedido();
    pedido.setId(pedidoId);
    
    RegistrarPagamentoDTO data = new RegistrarPagamentoDTO(
        StatusPagamento.PROCESSANDO,
        MetodoPagamento.PIX,
        BigDecimal.valueOf(100.00),
        pedidoId
    );
    
    when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
    
    Pagamento pagamento = new Pagamento();
    pagamento.setPedido(pedido);
    pagamento.setStatus(data.status());
    pagamento.setMetodo(data.metodo());
    pagamento.setValor(data.valor());
    
    when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);
    
    PagamentoDTO savedPagamento = pagamentoService.save(data);
    
    assertNotNull(savedPagamento);
    assertEquals(data.status(), savedPagamento.status());
    assertEquals(data.metodo(), savedPagamento.metodo());
    assertEquals(data.valor(), savedPagamento.valor());
    assertEquals(pedido.getId(), savedPagamento.pedidoId());
    verify(pagamentoRepository).save(any(Pagamento.class));
  }
  
  @Test
  void shouldThrowExceptionWhenPedidoNotFoundOnSave() {
    // Arrange
    Long pedidoId = 1L;
    RegistrarPagamentoDTO data = new RegistrarPagamentoDTO(
        StatusPagamento.PROCESSANDO,
        MetodoPagamento.PIX,
        BigDecimal.valueOf(100.00),
        pedidoId
    );
    
    when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());
    
    PedidoNotFoundException exception = assertThrows(PedidoNotFoundException.class,
        () -> pagamentoService.save(data));
    
    assertEquals("Pedido não encontrado", exception.getMessage());
  }
  
  @Test
  void shouldUpdatePagamentoSuccessfully() {
    // Arrange
    Long pedidoId = 1L;
    Long pagamentoId = 2L;
    
    Pedido pedido = new Pedido();
    pedido.setId(pedidoId);
    
    Pagamento pagamento = new Pagamento();
    pagamento.setStatus(StatusPagamento.PROCESSANDO);
    pagamento.setMetodo(MetodoPagamento.BOLETO);
    pagamento.setId(pagamentoId);
    pagamento.setPedido(pedido);
    
    pedido.setPagamento(pagamento);
    
    AtualizarPagamentoDTO data = new AtualizarPagamentoDTO(
        StatusPagamento.FINALIZADO,
        MetodoPagamento.PIX,
        LocalDateTime.now()
    );
    
    when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
    when(pagamentoRepository.findById(pagamentoId)).thenReturn(Optional.of(pagamento));
    
    pagamentoService.update(pedidoId, pagamentoId, data);
    
    verify(pagamentoRepository).save(any(Pagamento.class));
    assertEquals(data.status(), pagamento.getStatus());
    assertEquals(data.metodo(), pagamento.getMetodo());
    assertEquals(data.dataPagamento(), pagamento.getDataPagamento());
  }
  
  @Test
  void shouldThrowExceptionWhenPedidoNotFoundOnUpdate() {
    // Arrange
    Long pedidoId = 1L;
    Long pagamentoId = 2L;
    
    AtualizarPagamentoDTO data = new AtualizarPagamentoDTO(
        StatusPagamento.FINALIZADO,
        MetodoPagamento.PIX,
        LocalDateTime.now()
    );
    
    when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());
    
    PedidoNotFoundException exception = assertThrows(PedidoNotFoundException.class,
        () -> pagamentoService.update(pedidoId, pagamentoId, data));

    assertEquals("Pedido não encontrado", exception.getMessage());
  }
  
  @Test
  void shouldThrowExceptionWhenPagamentoNotFoundOnUpdate() {
    Long pedidoId = 1L;
    Long pagamentoId = 2L;
    
    Pedido pedido = new Pedido();
    pedido.setId(pedidoId);
    
    AtualizarPagamentoDTO data = new AtualizarPagamentoDTO(
        StatusPagamento.FINALIZADO,
        MetodoPagamento.PIX,
        LocalDateTime.now()
    );
    
    when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
    when(pagamentoRepository.findById(pagamentoId)).thenReturn(Optional.empty());
    
    PagamentoNotFoundException exception = assertThrows(PagamentoNotFoundException.class,
        () -> pagamentoService.update(pedidoId, pagamentoId, data));

    assertEquals("Pagamento não encontrado", exception.getMessage());
  }
  
  @Test
  void shouldThrowExceptionWhenPagamentoDoesNotMatchPedidoOnUpdate() {
    Long pedidoId = 1L;
    Long pagamentoId = 2L;
    
    Pedido pedido = new Pedido();
    pedido.setId(pedidoId);
    
    Pedido outroPedido = new Pedido();
    outroPedido.setId(3L);
    
    Pagamento pagamento = new Pagamento();
    pagamento.setId(pagamentoId);
    pagamento.setPedido(outroPedido);
    
    Pagamento pagamento2 = new Pagamento();
    pagamento2.setId(1L);
    pedido.setPagamento(pagamento2); // Pedido tem outro pagamento
    
    AtualizarPagamentoDTO data = new AtualizarPagamentoDTO(
        StatusPagamento.FINALIZADO,
        MetodoPagamento.PIX,
        LocalDateTime.now()
    );
    
    when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
    when(pagamentoRepository.findById(pagamentoId)).thenReturn(Optional.of(pagamento));
    
    PagamentoNotFoundException exception = assertThrows(PagamentoNotFoundException.class,
        () -> pagamentoService.update(pedidoId, pagamentoId, data));
    
    assertEquals("Pagamento do pedido " + pedidoId + " não encontrado", exception.getMessage());
  }
  
  @Test
  void shouldReturnPagamentoById() {
    Long pagamentoId = 1L;
    Pedido pedido = new Pedido();
    pedido.setId(1L);
    
    Pagamento pagamento = new Pagamento();
    pagamento.setId(pagamentoId);
    pagamento.setPedido(pedido);
    
    when(pagamentoRepository.findById(pagamentoId)).thenReturn(Optional.of(pagamento));
    
    PagamentoDTO result = pagamentoService.getById(pagamentoId);
    
    assertNotNull(result);
    assertEquals(pagamentoId, result.id());
  }
  
  @Test
  void shouldThrowExceptionWhenPagamentoNotFoundOnGetById() {
    Long pagamentoId = 1L;
    
    when(pagamentoRepository.findById(pagamentoId)).thenReturn(Optional.empty());
    
    PagamentoNotFoundException exception = assertThrows(PagamentoNotFoundException.class,
        () -> pagamentoService.getById(pagamentoId));
    
    assertEquals("Pagamento não encontrado", exception.getMessage());
  }
}