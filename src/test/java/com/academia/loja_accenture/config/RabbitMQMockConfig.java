package com.academia.loja_accenture.config;

import org.mockito.Mockito;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RabbitMQMockConfig {
  @Bean
  public AmqpTemplate amqpTemplate() {
    return Mockito.mock(AmqpTemplate.class);
  }
  
  @Bean
  public RabbitListener rabbitListener() {
    return Mockito.mock(RabbitListener.class);
  }
}