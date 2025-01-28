package com.academia.loja_accenture.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
            .group("loja_accenture")  // Nome do grupo da API (usado para segmentar rotas)
            .pathsToMatch("/**")      // Define que todas as rotas serão documentadas
            .build();
  }

  @Bean
  public OpenAPI pessoaAccentureAPI() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes(
                "bearer-key",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
            )
        )
        .info(informacoesApi()) // Definindo as informações da API
        .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
  }

  private Info informacoesApi() {
    Info apiInfo = new Info();

    apiInfo.title("Loja Accenture - API");  // Título da API
    apiInfo.description("Uma loja online simplificada para a Summer Academy da Accenture Brasil.");  // Descrição do propósito da API
    apiInfo.version("1.0");  // Versão da API
    apiInfo.license(new License().name("Licença - Accenture Academy").url("https://accenture.com/"));  // Informações sobre a licença de uso da API

    return apiInfo;
  }
}
