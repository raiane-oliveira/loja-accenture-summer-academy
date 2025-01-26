package com.academia.loja_accenture.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração do Swagger para a API da Loja Accenture.
 * Define como a documentação da API será gerada e configurada.
 */
@Configuration
public class SwaggerConfig {

  /**
   * Define o grupo da API pública e as rotas que serão documentadas.
   *
   * @return GroupedOpenApi que define o grupo de rotas para a documentação
   */
  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
            .group("loja_accenture")  // Nome do grupo da API (usado para segmentar rotas)
            .pathsToMatch("/**")      // Define que todas as rotas serão documentadas
            .build();
  }

  /**
   * Configura as informações básicas sobre a API, como título, descrição e versão.
   *
   * @return OpenAPI com as informações configuradas
   */
  @Bean
  public OpenAPI pessoaAccentureAPI() {
    return new OpenAPI()
            .info(informacoesApi());  // Definindo as informações da API
  }

  /**
   * Define as informações principais sobre a API, como título, descrição, versão e licença.
   *
   * @return Info com as informações da API
   */
  private Info informacoesApi() {
    Info apiInfo = new Info();

    apiInfo.title("Loja Accenture - API");  // Título da API
    apiInfo.description("Uma loja online simplificada para a Summer Academy da Accenture Brasil.");  // Descrição do propósito da API
    apiInfo.version("1.0");  // Versão da API
    apiInfo.license(new License().name("Licença - Accenture Academy").url("https://accenture.com/"));  // Informações sobre a licença de uso da API

    return apiInfo;
  }
}
