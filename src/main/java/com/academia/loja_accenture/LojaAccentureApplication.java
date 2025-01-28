package com.academia.loja_accenture;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class LojaAccentureApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojaAccentureApplication.class, args);
	}
}
