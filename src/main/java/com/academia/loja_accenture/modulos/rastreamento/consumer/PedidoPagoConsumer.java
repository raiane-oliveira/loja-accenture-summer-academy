package com.academia.loja_accenture.modulos.rastreamento.consumer;

import com.academia.loja_accenture.core.exceptions.InvalidJsonException;
import com.academia.loja_accenture.modulos.pedido.dto.PedidoQueuePayload;
import com.academia.loja_accenture.modulos.rastreamento.domain.StatusEnum;
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
public class PedidoPagoConsumer {

  private final StatusPedidoService statusPedidoService;
  private final ObjectMapper objectMapper;

  @RabbitListener(queues = { "equipeum.pedidos.pagos" })
  public void receive(String message) {
    log.info("Recebida mensagem de pedido pago: {}", message);

    TypeReference<PedidoQueuePayload> mapType = new TypeReference<>() {};
    PedidoQueuePayload pedido;

    try {
      pedido = objectMapper.readValue(message, mapType);
    } catch (JsonProcessingException e) {
      log.error("Erro ao processar JSON do pedido pago: {}", e.getMessage());
      throw new InvalidJsonException("Erro ao processar pedido pago: " + e.getMessage());
    }

    log.info("Pedido #{} foi processado com sucesso", pedido.id());

    RegistrarStatusRequestDTO statusPedido = new RegistrarStatusRequestDTO();
    statusPedido.setStatus(StatusEnum.PROCESSANDO);

    statusPedidoService.registrarStatus(pedido.id(), statusPedido);

    log.info("Status do Pedido #{} atualizado para PROCESSANDO", pedido.id());
  }
}
