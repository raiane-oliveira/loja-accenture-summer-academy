package com.academia.loja_accenture.modulos.rastreamento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import com.academia.loja_accenture.core.exceptions.PedidoNotFoundException;
import com.academia.loja_accenture.core.exceptions.ResourceNotFoundException;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import com.academia.loja_accenture.modulos.rastreamento.domain.StatusEnum;
import com.academia.loja_accenture.modulos.rastreamento.domain.StatusPedido;
import com.academia.loja_accenture.modulos.rastreamento.dto.RegistrarStatusRequestDTO;
import com.academia.loja_accenture.modulos.rastreamento.dto.RegistrarStatusResponseDTO;
import com.academia.loja_accenture.modulos.rastreamento.repository.StatusPedidoRepository;
import com.academia.loja_accenture.modulos.rastreamento.service.StatusPedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatusPedidoServiceTest {
  
  @Mock
  private PedidoRepository pedidoRepository;
  
  @Mock
  private StatusPedidoRepository statusPedidoRepository;
  
  @InjectMocks
  private StatusPedidoService statusPedidoService;
  
  private Pedido pedido;
  private StatusPedido statusPedido;
  private RegistrarStatusRequestDTO requestDTO;
  private RegistrarStatusResponseDTO responseDTO;
  
  @BeforeEach
  void setUp() {
    pedido = new Pedido();
    pedido.setId(1L);
    
    statusPedido = new StatusPedido();
    statusPedido.setId(1L);
    statusPedido.setPedido(pedido);
    statusPedido.setStatus(StatusEnum.ENTREGUE);
    
    requestDTO = new RegistrarStatusRequestDTO();
    requestDTO.setStatus(StatusEnum.ENTREGUE);
    
    responseDTO = new RegistrarStatusResponseDTO(
        statusPedido.getId(),
        statusPedido.getStatus(),
        statusPedido.getCreatedAt()
    );
  }
  
  @Test
  void registrarStatus_DeveRegistrarStatusQuandoPedidoExiste() {
    when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
    when(statusPedidoRepository.save(any(StatusPedido.class))).thenReturn(statusPedido);
    
    RegistrarStatusResponseDTO response = statusPedidoService.registrarStatus(1L, requestDTO);
    
    assertNotNull(response);
    assertEquals(1L, response.getId());
    assertEquals(StatusEnum.ENTREGUE, response.getStatus());
  }
  
  @Test
  void registrarStatus_DeveLancarExcecaoQuandoPedidoNaoExiste() {
    when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());
    
    assertThrows(PedidoNotFoundException.class, () ->
        statusPedidoService.registrarStatus(1L, requestDTO)
    );
  }
  
  @Test
  void obterHistorico_DeveRetornarListaDeStatusQuandoHistoricoExiste() {
    when(pedidoRepository.existsById(1L)).thenReturn(true);
    when(statusPedidoRepository.findByPedidoIdOrderByCreatedAtDesc(1L))
        .thenReturn(Arrays.asList(statusPedido));
    
    List<RegistrarStatusResponseDTO> historico = statusPedidoService.obterHistorico(1L);
    
    assertFalse(historico.isEmpty());
    assertEquals(1, historico.size());
    assertEquals(StatusEnum.ENTREGUE, historico.getFirst().getStatus());
  }
  
  @Test
  void obterHistorico_DeveLancarExcecaoQuandoPedidoNaoExiste() {
    when(pedidoRepository.existsById(1L)).thenReturn(false);
    
    assertThrows(PedidoNotFoundException.class, () ->
        statusPedidoService.obterHistorico(1L)
    );
  }
  
  @Test
  void obterStatusAtual_DeveRetornarStatusAtualQuandoExiste() {
    when(pedidoRepository.existsById(1L)).thenReturn(true);
    when(statusPedidoRepository.findTopByPedidoIdOrderByCreatedAtDesc(1L))
        .thenReturn(Optional.of(statusPedido));
    
    RegistrarStatusResponseDTO response = statusPedidoService.obterStatusAtual(1L);
    
    assertNotNull(response);
    assertEquals(StatusEnum.ENTREGUE, response.getStatus());
  }
  
  @Test
  void obterStatusAtual_DeveLancarExcecaoQuandoNaoExisteStatus() {
    when(pedidoRepository.existsById(1L)).thenReturn(true);
    when(statusPedidoRepository.findTopByPedidoIdOrderByCreatedAtDesc(1L))
        .thenReturn(Optional.empty());
    
    assertThrows(ResourceNotFoundException.class, () ->
        statusPedidoService.obterStatusAtual(1L)
    );
  }
}
