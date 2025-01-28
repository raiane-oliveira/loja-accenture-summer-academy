package com.academia.loja_accenture.modulos.rastreamento.consumer;

import com.academia.loja_accenture.core.exceptions.InvalidJsonException;
import com.academia.loja_accenture.modulos.pedido.dto.PedidoQueuePayload;
import com.academia.loja_accenture.modulos.rastreamento.dto.RegistrarStatusRequestDTO;
import com.academia.loja_accenture.modulos.rastreamento.service.StatusPedidoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCanceladoConsumer {
   private final StatusPedidoService statusPedidoService;
   private final ObjectMapper objectMapper;
  
  @RabbitListener(queues = { "equipeum.pedidos.cancelados" })
   public void receive(String message) {
     TypeReference<PedidoQueuePayload> mapType = new TypeReference<>() {};
     PedidoQueuePayload pedido;
     
     try {
       pedido = objectMapper.readValue(message, mapType);
     } catch (JsonProcessingException e) {
       throw new InvalidJsonException("Erro ao processar pedido cancelado");
     }
    
    log.error("Pedido #{} cancelado por falha no pagamento", pedido.id());
    RegistrarStatusRequestDTO statusPedido = new RegistrarStatusRequestDTO();
    statusPedido.setDescricao("Pedido cancelado por falha no pagamento");
    statusPedidoService.registrarStatus(pedido.id(), statusPedido);
   }
}
