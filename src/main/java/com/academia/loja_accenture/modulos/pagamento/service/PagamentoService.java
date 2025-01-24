package com.academia.loja_accenture.modulos.pagamento.service;

import com.academia.loja_accenture.modulos.pagamento.domain.Pagamento;
import com.academia.loja_accenture.modulos.pagamento.dto.AtualizarPagamentoDTO;
import com.academia.loja_accenture.modulos.pagamento.dto.PagamentoDTO;
import com.academia.loja_accenture.modulos.pagamento.dto.RegistrarPagamentoDTO;
import com.academia.loja_accenture.modulos.pagamento.repository.PagamentoRepository;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {
  private final PagamentoRepository pagamentoRepository;
  private final PedidoRepository pedidoRepository;
  
  public PagamentoDTO save(RegistrarPagamentoDTO data) {
    Pedido pedido = pedidoRepository.findById(data.pedidoId())
        .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
    
    Pagamento pagamento = new Pagamento();
    pagamento.setStatus(data.status());
    pagamento.setMetodo(data.metodo());
    pagamento.setValor(data.valor());
    pagamento.setDataPagamento(null);
    pagamento.setPedido(pedido);
    
    return convertToDTO(pagamentoRepository.save(pagamento));
  }
  
  public void update(Long pedidoId, Long pagamentoId, AtualizarPagamentoDTO data) {
    Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(
        () -> new IllegalArgumentException("Pedido não encontrado"));
    
    Pagamento pagamento = pagamentoRepository.findById(pagamentoId).orElseThrow(
        () -> new IllegalArgumentException("Pagamento não encontrado"));
    
    boolean successful = pedido.getPagamento().equals(pagamento);
    if (!successful) {
      throw new IllegalArgumentException("Pagamento do pedido " + pedidoId + " não encontrado");
    }
    
    if (data.status() != null) {
      pagamento.setStatus(data.status());
    }
    if (data.metodo() != null) {
      pagamento.setMetodo(data.metodo());
    }
    if (data.dataPagamento() != null) {
      pagamento.setDataPagamento(data.dataPagamento());
    }
    
    pagamentoRepository.save(pagamento);
  }
  
  public PagamentoDTO getById(Long id) {
    Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow(
        () -> new IllegalArgumentException("Pagamento não encontrado"));
    
    return convertToDTO(pagamento);
  }
  
  private PagamentoDTO convertToDTO(Pagamento pagamento) {
    return new PagamentoDTO(
        pagamento.getId(),
        pagamento.getStatus(),
        pagamento.getMetodo(),
        pagamento.getValor(),
        pagamento.getDataPagamento(),
        pagamento.getPedido().getId(),
        pagamento.getCreatedAt()
    );
  }
}
