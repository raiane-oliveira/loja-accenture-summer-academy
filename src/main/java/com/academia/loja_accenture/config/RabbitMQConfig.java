package com.academia.loja_accenture.config;

import com.academia.loja_accenture.core.queue.RabbitQueue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  @Bean
//  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    
    container.setConnectionFactory(connectionFactory);
    
    container.setQueueNames(RabbitQueue.PEDIDOS_REGISTRADOS.getName());
    
    return container;
  }
  
//  @Bean
//  MessageListenerAdapter listenerAdapter() {
//    return new MessageListenerAdapter();
//  }
}
