package com.academia.loja_accenture.modulos.pagamento.consumer;

import com.academia.loja_accenture.core.exceptions.InvalidJsonException;
import com.academia.loja_accenture.core.message_broker.Exchanges;
import com.academia.loja_accenture.core.message_broker.RoutingKeys;
import com.academia.loja_accenture.modulos.pagamento.domain.MetodoPagamento;
import com.academia.loja_accenture.modulos.pagamento.domain.StatusPagamento;
import com.academia.loja_accenture.modulos.pagamento.dto.AtualizarPagamentoDTO;
import com.academia.loja_accenture.modulos.pagamento.dto.RegistrarPagamentoDTO;
import com.academia.loja_accenture.modulos.pagamento.service.PagamentoService;
import com.academia.loja_accenture.modulos.pedido.dto.PedidoQueuePayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PagamentoPedidoRegistradoConsumer {
   private final PagamentoService pagamentoService;
   private final AmqpTemplate amqpTemplate;
   private final ObjectMapper objectMapper;
   
  @RabbitListener(queues = { "equipeum.pedidos.registrados" })
   public void receive(String message) {
      TypeReference<PedidoQueuePayload> mapType = new TypeReference<>() {};
      PedidoQueuePayload pedido;
      
      try {
        pedido = objectMapper.readValue(message, mapType);
      } catch (JsonProcessingException e) {
        throw new InvalidJsonException("Erro ao processar pedido");
      }
      
      RegistrarPagamentoDTO pagamentoData = new RegistrarPagamentoDTO(
          StatusPagamento.PROCESSANDO,
          MetodoPagamento.CREDITO,
          pedido.valor(),
          pedido.id()
      );
     
      var pagamento = pagamentoService.save(pagamentoData);
     
      // Simulação, processamento do pagamento
      // Lógica para testar os dois fluxos: pagamentos finalizados e cancelados
      boolean isSuccessful = (int) (Math.random() * 100) < 80;
    
      if (!isSuccessful) {
         AtualizarPagamentoDTO pagamentoCancelado = new AtualizarPagamentoDTO(StatusPagamento.CANCELADO, null, null);
         pagamentoService.update(pedido.id(), pagamento.id(), pagamentoCancelado);
         
         amqpTemplate.convertAndSend(Exchanges.PEDIDOS.getName(), RoutingKeys.PEDIDO_CANCELADO.getName(), message);
         log.info("Pedido #{} enviado para a fila de pedidos cancelados", pedido.id());
      } else {
        AtualizarPagamentoDTO pagamentoFinalizado = new AtualizarPagamentoDTO(StatusPagamento.FINALIZADO, null, LocalDateTime.now());
        pagamentoService.update(pedido.id(), pagamento.id(), pagamentoFinalizado);
       
        amqpTemplate.convertAndSend(Exchanges.PEDIDOS_PAGOS.getName(), RoutingKeys.PEDIDO_PAGO.getName(), message);
        log.info("Pedido #{} enviado para a fila de pedidos pagos", pedido.id());
      }
   }
}
