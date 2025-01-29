# Loja Accenture - Sistema de Rastreamento de Pedidos

## Descrição
Este projeto é uma aplicação Java desenvolvida com **Spring Boot** para gerenciar pedidos, histórico de status e funcionalidades de rastreamento. A aplicação também utiliza **Swagger** para documentação da API, **RabbitMQ** para comunicação assíncrona e **JPA** para persistência de dados.

## Integrantes do Grupo
- **Bruna Neves**
- **Larissa Arruda**
- **Natália Viana**
- **Raiane Oliveira**

## Sumário
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Como Rodar o Projeto](#como-rodar-o-projeto)
- [Endpoints da API](#endpoints-da-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Modelo de Dados (ER Diagram)](#modelo-de-dados-er-diagram)
- [Testes](#testes)
  
## Introdução:
Este sistema foi desenvolvido com o intuito de gerenciar pedidos de forma eficiente, com foco na atualização em tempo real dos status dos pedidos, rastreamento histórico e comunicação assíncrona. A utilização de Spring Boot, RabbitMQ e Swagger tornam a aplicação moderna e de fácil integração.

## Funcionalidades
- Gerenciamento de pedidos e vendedores.
- Rastreamento do histórico de status dos pedidos.
- Registro e consulta de status em tempo real.
- Interface de documentação com **Swagger**.
- Persistência de dados com banco de dados relacional.
- **Mensageria assíncrona com RabbitMQ**.

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3.x**
- **JPA/Hibernate**
- **H2 Database** (para desenvolvimento e testes) ou **MySQL/PostgreSQL** (para produção)
- **Swagger/OpenAPI 3** para documentação
- **RabbitMQ 3.2.1** para comunicação assíncrona
- **JUnit 5** para testes unitários

## Configuração do Ambiente
### Pré-requisitos
- **Java 21** instalado.
- **Maven** instalado para gerenciamento de dependências.
- **Banco de dados configurado** (opcional para uso local).
- **RabbitMQ instalado e configurado**.
- **Ferramenta de controle de versão Git**.
- **IDE como IntelliJ IDEA ou Eclipse** (opcional).

## Como Rodar o Projeto
### 1. Clonar o repositório
```sh
git clone https://github.com/seu-usuario/loja-accenture.git
cd loja-accenture
```

### 2. Configurar o arquivo de propriedades
Configure o arquivo `application.properties` para apontar para seu banco de dados:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/loja_accenture
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

### 3. Compilar e executar
```sh
mvn spring-boot:run
```
A aplicação estará disponível em:
```
http://localhost:8080
```

## Endpoints da API
### Base URL: `http://localhost:8080/api`
#### Endpoints de Vendedores
| Método | Endpoint | Descrição |
|--------|---------|-----------|
| **POST** | `/vendedores` | Cadastrar um novo vendedor |
| **GET** | `/vendedores` | Listar todos os vendedores |
| **GET** | `/vendedores/{id}` | Buscar vendedor por ID |
| **PUT** | `/vendedores/{id}` | Atualizar informações do vendedor |

#### Endpoints de Rastreamento
| Método | Endpoint | Descrição |
|--------|---------|-----------|
| **POST** | `/status-pedidos` | Registrar um novo status |
| **GET** | `/status-pedidos/{id}` | Obter histórico de status por ID |
| **GET** | `/status-pedidos/{id}/atual` | Obter o status atual do pedido |

## Estrutura do Projeto
```
src/main/java/com/academia/loja_accenture
├── modulos
│   ├── usuario
│   │   ├── controller
│   │   ├── domain
│   │   ├── dto
│   │   ├── repository
│   │   └── service
│   ├── pedido
│   │   ├── controller
│   │   ├── domain
│   │   ├── repository
│   │   └── service
│   ├── rastreamento
│   │   ├── controller
│   │   ├── domain
│   │   ├── dto
│   │   ├── repository
│   │   └── service
│   └── mensageria
│       ├── producer
│       ├── consumer
```

## Modelo de Dados (ER Diagram)
![ER Diagram](https://github.com/nataliatviana/readmetest/blob/main/diagrama%20loja%20atualizado.png?raw=true)

### Descrição das Entidades
#### **Tabela: `vendedor`**
| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | BIGINT (PK) | Identificador único do vendedor |
| `nome` | VARCHAR(255) | Nome do vendedor |
| `email` | VARCHAR(255) | E-mail do vendedor |

#### **Tabela: `pedido`**
| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | BIGINT (PK) | Identificador único do pedido |
| `status` | VARCHAR(50) | Status atual do pedido |
| `vendedor_id` | BIGINT (FK) | Identificador do vendedor responsável |

#### **Tabela: `historico_status`**
| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | BIGINT (PK) | Identificador único do histórico |
| `pedido_id` | BIGINT (FK) | Pedido relacionado |
| `status` | VARCHAR(50) | Status registrado |
| `data_atualizacao` | TIMESTAMP | Data da atualização do status |

## Testes
Os testes estão localizados no diretório `src/test/java` e incluem:
- **Testes unitários para serviços.**
- **Testes de integração para endpoints.**

### Executar os testes
```sh
mvn test
```

