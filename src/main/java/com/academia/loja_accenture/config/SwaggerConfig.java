package com.academia.loja_accenture.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("loja_accenture")
        .pathsToMatch("/**")
        .build();
  }
  
  @Bean
  public OpenAPI pessoaAccentureAPI() {
    return new OpenAPI()
        .info(informacoesApi());
  }
  private Info informacoesApi() {
    Info apiInfo = new Info();
    
    apiInfo.title("Loja Accenture - API");
    apiInfo.description("Uma loja online simplificada para a Summer Academy da Accenture Brasil.");
    apiInfo.version("1.0");
    apiInfo.license(new License().name("Licença - Accenture Academy").url("https://accenture.com/"));
    
    return apiInfo;
    
  }
}
