package com.academia.loja_accenture.config;

import com.academia.loja_accenture.core.message_broker.Exchanges;
import com.academia.loja_accenture.core.message_broker.Queues;
import com.academia.loja_accenture.core.message_broker.RoutingKeys;
import com.academia.loja_accenture.modulos.pagamento.listener.PagamentoListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfigProducer {
  @Bean
  Queue queuePedidosRegistrados() {
   return new Queue(Queues.PEDIDOS_REGISTRADOS.getName(), true);
  }
  
  @Bean
  DirectExchange pedidosExchange() {
    return new DirectExchange(Exchanges.PEDIDOS.getName(), true, false);
  }
  
  @Bean
  Binding bindingPedidos(Queue queuePedidosRegistrados, DirectExchange pedidosExchange) {
    return BindingBuilder.bind(queuePedidosRegistrados).to(pedidosExchange).with(RoutingKeys.PEDIDO_REGISTRADO.getName());
  }
  
  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter pagamentoListenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    
    container.setConnectionFactory(connectionFactory);
    container.setMessageListener(pagamentoListenerAdapter);
    
    container.setQueueNames(Queues.PEDIDOS_REGISTRADOS.getName());
    
    return container;
  }
  
  @Bean
  MessageListenerAdapter pagamentoListenerAdapter(PagamentoListener pagamentoListener) {
    return new MessageListenerAdapter(pagamentoListener, "processarPedido");
  }
}
