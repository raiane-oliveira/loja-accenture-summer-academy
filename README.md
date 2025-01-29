# Loja Accenture - Sistema de Rastreamento de Pedidos

## Descrição
Este projeto é uma aplicação Java desenvolvida com **Spring Boot** para gerenciar pedidos, histórico de status e funcionalidades de rastreamento. A aplicação também utiliza **Swagger** para documentação da API, **RabbitMQ** para comunicação assíncrona e **JPA** para persistência de dados.

## Integrantes do Grupo
- **[Bruna Neves](https://github.com/ibrunaneves)**
- **[Larissa Arruda](https://github.com/LarissaArruda08)**
- **[Natália Viana](https://github.com/nataliatviana)**
- **[Raiane Oliveira](https://github.com/raiane-oliveira)**

## Sumário
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Como Rodar o Projeto](#como-rodar-o-projeto)
- [Endpoints da API](#endpoints-da-api)
- [Rastreamento de Pedidos](#rastreamento-de-pedidos)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Modelo de Dados (ER Diagram)](#modelo-de-dados-er-diagram)
- [Testes](#testes)
  
## Introdução
Este sistema foi desenvolvido com o intuito de gerenciar pedidos de forma eficiente, com foco na atualização em tempo real dos status dos pedidos, rastreamento histórico e comunicação assíncrona. A utilização de Spring Boot, RabbitMQ e Swagger tornam a aplicação moderna e de fácil integração.

## Funcionalidades
- Gerenciamento de pedidos e vendedores.
- Rastreamento do histórico de status dos pedidos.
- Registro e consulta de status em tempo real.
- Interface de documentação com **Swagger**.
- Persistência de dados com banco de dados relacional.
- **Mensageria assíncrona com RabbitMQ**.

## Rastreamento de Pedidos
Cada pedido tem um status que pode mudar ao longo do tempo. As transições de status são controladas por eventos disparados no sistema.

### **Fluxo de Status**
Os pedidos passam pelos seguintes status:
1. **CRIADO** - O pedido foi registrado pelo cliente.
2. **PAGO** - O pagamento foi confirmado.
3. **EM PREPARAÇÃO** - O pedido está sendo processado pelo vendedor.
4. **ENVIADO** - O pedido foi enviado para o cliente.
5. **ENTREGUE** - O cliente recebeu o pedido.
6. **CANCELADO** - O pedido foi cancelado antes da entrega.

Cada mudança de status dispara um **evento RabbitMQ**, notificando os sistemas interessados sobre a atualização.

### **Fluxo de Mensagens no RabbitMQ**
1. Quando um pedido é criado, ele é enviado para a fila `pedido.criado`.
2. O serviço de pagamentos consome essa mensagem e, se aprovado, publica na fila `pedido.pago`.
3. O serviço de logística escuta `pedido.pago`, prepara o envio e publica em `pedido.enviado`.
4. O sistema de rastreamento escuta `pedido.enviado` e, ao receber confirmação de entrega, envia `pedido.entregue`.

Isso garante um **fluxo de pedidos assíncrono e escalável**, sem bloqueios entre serviços.

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3.x**
- **JPA/Hibernate**
- **H2 Database** (para desenvolvimento e testes) ou **MySQL/PostgreSQL** (para produção)
- **Swagger/OpenAPI 3** para documentação
- **RabbitMQ 3.2.1** para comunicação assíncrona
- **JUnit 5** para testes unitários

## Modelo de Dados (ER Diagram)
![ER Diagram](https://github.com/nataliatviana/readmetest/blob/main/diagrama%20loja%20atualizado.png?raw=true)

### **Descrição das Entidades**
#### **Tabela: `vendedor`**
| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | BIGINT (PK) | Identificador único do vendedor |
| `nome` | VARCHAR(255) | Nome do vendedor |
| `setor` | VARCHAR(255) | Setor do vendedor |
| `email` | VARCHAR(255) | E-mail do vendedor |
| `senha` | VARCHAR(255) | Senha do vendedor |

#### **Tabela: `cliente`**
| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | BIGINT (PK) | Identificador único do cliente |
| `nome` | VARCHAR(255) | Nome do cliente |
| `email` | VARCHAR(255) | E-mail do cliente |
| `senha` | VARCHAR(255) | Senha do cliente |

#### **Tabela: `produto`**
| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | BIGINT (PK) | Identificador único do produto |
| `nome` | VARCHAR(255) | Nome do produto |
| `descricao` | VARCHAR(255) | Descrição do produto |
| `valor` | DECIMAL(10,2) | Valor do produto |
| `created_at` | DATETIME | Data de criação do produto |
| `vendedor_id` | BIGINT (FK) | Identificador do vendedor |

#### **Tabela: `estoque`**
| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | BIGINT (PK) | Identificador único do estoque |
| `nome` | VARCHAR(255) | Nome do estoque |
| `quantidade` | BIGINT | Quantidade em estoque |
| `created_at` | DATETIME | Data de criação |
| `produto_id` | BIGINT (FK) | Produto relacionado |

#### **Tabela: `pagamento`**
| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | BIGINT (PK) | Identificador único do pagamento |
| `status` | SMALLINT | Status do pagamento |
| `metodo` | SMALLINT | Método de pagamento |
| `valor` | DECIMAL(10,2) | Valor pago |
| `data_pagamento` | DATETIME | Data do pagamento |
| `created_at` | DATETIME | Data de criação |
| `pedido_id` | BIGINT (FK) | Pedido relacionado |

#### **Tabela: `pedido`**
| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | BIGINT (PK) | Identificador único do pedido |
| `descricao` | VARCHAR(255) | Descrição do pedido |
| `valor` | DECIMAL(10,2) | Valor total do pedido |
| `quantidade` | INT | Quantidade de itens no pedido |
| `created_at` | DATETIME | Data de criação |
| `cliente_id` | BIGINT (FK) | Cliente relacionado |
| `vendedor_id` | BIGINT (FK) | Vendedor responsável |

#### **Tabela: historico_status**
| Coluna | Tipo | Descrição |
|--------|------|-----------|
| id | BIGINT (PK) | Identificador único do histórico |
| pedido_id | BIGINT (FK) | Pedido relacionado |
| status | VARCHAR(50) | Status registrado |
| data_atualizacao | TIMESTAMP | Data da atualização do status |

## Testes
Os testes estão localizados no diretório `src/test/java` e incluem:
- **Testes unitários para serviços.**
- **Testes de integração para endpoints.**

### Executar os testes
```sh
mvn test
```

