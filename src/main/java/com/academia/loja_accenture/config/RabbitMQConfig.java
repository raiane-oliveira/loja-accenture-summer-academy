package com.academia.loja_accenture.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
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
  Queue queuePedidosCancelados() {
    return new Queue(Queues.PEDIDOS_CANCELADOS.getName(), true);
  }

  @Bean
  DirectExchange pedidosExchange() {
    return new DirectExchange(Exchanges.PEDIDOS.getName(), true, false);
  }
  
  @Bean
  Binding bindingPedidosRegistrados(Queue queuePedidosRegistrados, DirectExchange pedidosExchange) {
    return BindingBuilder.bind(queuePedidosRegistrados).to(pedidosExchange)
        .with(RoutingKeys.PEDIDO_REGISTRADO.getName());
  }
  
  @Bean
  Binding bindingPedidosPagos(Queue queuePedidosPagos, DirectExchange pedidosExchange) {
    return BindingBuilder.bind(queuePedidosPagos).to(pedidosExchange)
        .with(RoutingKeys.PEDIDO_PAGO.getName());
  }
  
  @Bean
  Binding bindingPedidosCancelados(Queue queuePedidosCancelados, DirectExchange pedidosExchange) {
    return BindingBuilder.bind(queuePedidosCancelados).to(pedidosExchange)
        .with(RoutingKeys.PEDIDO_CANCELADO.getName());
  }

//  @Bean
//  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
//    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//
//    container.setConnectionFactory(connectionFactory);
//    container.setQueueNames(
//        Queues.PEDIDOS_REGISTRADOS.getName(),
//        Queues.PEDIDOS_PAGOS.getName(),
//        Queues.PEDIDOS_CANCELADOS.getName());
//
//    return container;
//  }
}
