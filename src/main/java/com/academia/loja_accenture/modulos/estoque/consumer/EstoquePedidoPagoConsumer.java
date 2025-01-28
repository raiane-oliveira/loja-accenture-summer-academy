package com.academia.loja_accenture.modulos.estoque.consumer;

import com.academia.loja_accenture.core.exceptions.InvalidJsonException;
import com.academia.loja_accenture.modulos.estoque.service.EstoqueService;
import com.academia.loja_accenture.modulos.pedido.dto.PedidoQueuePayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EstoquePedidoPagoConsumer {
   private final EstoqueService estoqueService;
   private final ObjectMapper objectMapper;
  
  @RabbitListener(queues = { "equipeum.estoque.pedidos.pagos" })
   public void receive(String message) {
     TypeReference<PedidoQueuePayload> mapType = new TypeReference<>() {};
     PedidoQueuePayload pedido;
     
     try {
       pedido = objectMapper.readValue(message, mapType);
     } catch (JsonProcessingException e) {
       throw new InvalidJsonException("Erro ao processar pedido pago");
     }
    
     estoqueService.diminuirQuantidadePorProdutosId(pedido.produtos());
     log.info("Pedido #{} foi diminuído do estoque", pedido.id());
   }
}
