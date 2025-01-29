package com.academia.loja_accenture.factory;

import com.academia.loja_accenture.modulos.pagamento.domain.MetodoPagamento;
import com.academia.loja_accenture.modulos.pagamento.domain.Pagamento;
import com.academia.loja_accenture.modulos.pagamento.domain.StatusPagamento;

import java.math.BigDecimal;

public class MakePagamento {
  public static Pagamento create() {
    Pagamento pagamento = new Pagamento();
    pagamento.setId(null);
    pagamento.setStatus(StatusPagamento.FINALIZADO);
    pagamento.setMetodo(MetodoPagamento.CREDITO);
    pagamento.setValor(BigDecimal.TEN);
    
    return pagamento;
  }
  
  public static Pagamento create(Long id) {
    Pagamento pagamento = new Pagamento();
    pagamento.setId(id);
    return pagamento;
  }
  
  public static Pagamento create(Long id, StatusPagamento status, MetodoPagamento metodo, BigDecimal valor) {
    Pagamento pagamento = new Pagamento();
    pagamento.setId(id);
    pagamento.setStatus(status);
    pagamento.setMetodo(metodo);
    pagamento.setValor(valor);
    
    return pagamento;
  }
}
