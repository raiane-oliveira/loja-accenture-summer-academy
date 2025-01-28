package com.academia.loja_accenture.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.academia.loja_accenture.core.message_broker.Exchanges;
import com.academia.loja_accenture.core.message_broker.Queues;
import com.academia.loja_accenture.core.message_broker.RoutingKeys;

@Configuration
public class RabbitMQConfig {
  @Bean
  Queue queuePedidosRegistrados() {
    return new Queue(Queues.PEDIDOS_REGISTRADOS.getName(), true);
  }
  
  @Bean
  Queue queuePedidosPagos() {
    return new Queue(Queues.PEDIDOS_PAGOS.getName(), true);
  }
  
  @Bean
  Queue queuePedidosPagosEstoque() {
    return new Queue(Queues.ESTOQUE_PEDIDOS_PAGOS.getName(), true);
  }
  
  @Bean
  Queue queuePedidosCancelados() {
    return new Queue(Queues.PEDIDOS_CANCELADOS.getName(), true);
  }
  
  @Bean
  DirectExchange pedidosExchange() {
    return new DirectExchange(Exchanges.PEDIDOS.getName(), true, false);
  }
  
  @Bean
  TopicExchange pedidosPagosExchange() {
    return new TopicExchange(Exchanges.PEDIDOS_PAGOS.getName(), true, false);
  }
  
  @Bean
  Binding bindingPedidosRegistrados(Queue queuePedidosRegistrados, DirectExchange pedidosExchange) {
    return BindingBuilder.bind(queuePedidosRegistrados).to(pedidosExchange)
        .with(RoutingKeys.PEDIDO_REGISTRADO.getName());
  }
  
  @Bean
  Binding bindingPedidosPagos(Queue queuePedidosPagos, TopicExchange pedidosPagosExchange) {
    return BindingBuilder.bind(queuePedidosPagos).to(pedidosPagosExchange).with(RoutingKeys.PEDIDO_PAGO.getName());
  }
  
  @Bean
  Binding bindingPedidosPagosEstoque(Queue queuePedidosPagosEstoque, TopicExchange pedidosExchange) {
    return BindingBuilder.bind(queuePedidosPagosEstoque).to(pedidosExchange)
        .with(RoutingKeys.PEDIDO_PAGO.getName());
  }
  
  @Bean
  Binding bindingPedidosCancelados(Queue queuePedidosCancelados, DirectExchange pedidosExchange) {
    return BindingBuilder.bind(queuePedidosCancelados).to(pedidosExchange)
        .with(RoutingKeys.PEDIDO_CANCELADO.getName());
  }
}
