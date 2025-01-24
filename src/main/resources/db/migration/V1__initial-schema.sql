CREATE TABLE cliente
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    nome  VARCHAR(255)          NOT NULL,
    email VARCHAR(255)          NOT NULL,
    senha VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_cliente PRIMARY KEY (id)
);

CREATE TABLE estoque
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)          NOT NULL,
    quantidade BIGINT                NOT NULL,
    created_at datetime              NOT NULL,
    produto_id BIGINT                NULL,
    CONSTRAINT pk_estoque PRIMARY KEY (id)
);

CREATE TABLE pagamento
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    status         SMALLINT              NOT NULL,
    metodo         SMALLINT              NOT NULL,
    valor          DECIMAL(10, 2)        NOT NULL,
    data_pagamento datetime              NOT NULL,
    created_at     datetime              NOT NULL,
    pedido_id      BIGINT                NOT NULL,
    CONSTRAINT pk_pagamento PRIMARY KEY (id)
);

CREATE TABLE pedido
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    descricao   VARCHAR(255)          NOT NULL,
    valor       DECIMAL(10, 2)        NOT NULL,
    quantidade  INT                   NOT NULL,
    created_at  datetime              NOT NULL,
    cliente_id  BIGINT                NULL,
    vendedor_id BIGINT                NULL,
    CONSTRAINT pk_pedido PRIMARY KEY (id)
);

CREATE TABLE pedido_historico_status
(
    pedido_id        BIGINT NOT NULL,
    status_pedido_id BIGINT NOT NULL,
    CONSTRAINT pk_pedido_historico_status PRIMARY KEY (pedido_id, status_pedido_id)
);

CREATE TABLE produto
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    nome        VARCHAR(255)          NOT NULL,
    descricao   VARCHAR(255)          NOT NULL,
    valor       DECIMAL(10, 2)        NOT NULL,
    created_at  datetime              NULL,
    vendedor_id BIGINT                NOT NULL,
    CONSTRAINT pk_produto PRIMARY KEY (id)
);

CREATE TABLE produto_tem_pedido
(
    pedido_id  BIGINT NOT NULL,
    produto_id BIGINT NOT NULL
);

CREATE TABLE status_pedido
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    descricao  VARCHAR(255)          NOT NULL,
    created_at datetime              NOT NULL,
    CONSTRAINT pk_status_pedido PRIMARY KEY (id)
);

CREATE TABLE vendedor
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    nome  VARCHAR(255)          NOT NULL,
    setor VARCHAR(255)          NOT NULL,
    email VARCHAR(255)          NOT NULL,
    senha VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_vendedor PRIMARY KEY (id)
);

ALTER TABLE cliente
    ADD CONSTRAINT uc_cliente_email UNIQUE (email);

ALTER TABLE estoque
    ADD CONSTRAINT uc_estoque_produto UNIQUE (produto_id);

ALTER TABLE pagamento
    ADD CONSTRAINT uc_pagamento_pedido UNIQUE (pedido_id);

ALTER TABLE vendedor
    ADD CONSTRAINT uc_vendedor_email UNIQUE (email);

ALTER TABLE estoque
    ADD CONSTRAINT FK_ESTOQUE_ON_PRODUTO FOREIGN KEY (produto_id) REFERENCES produto (id);

ALTER TABLE pagamento
    ADD CONSTRAINT FK_PAGAMENTO_ON_PEDIDO FOREIGN KEY (pedido_id) REFERENCES pedido (id);

ALTER TABLE pedido
    ADD CONSTRAINT FK_PEDIDO_ON_CLIENTE FOREIGN KEY (cliente_id) REFERENCES cliente (id);

ALTER TABLE pedido
    ADD CONSTRAINT FK_PEDIDO_ON_VENDEDOR FOREIGN KEY (vendedor_id) REFERENCES vendedor (id);

ALTER TABLE produto
    ADD CONSTRAINT FK_PRODUTO_ON_VENDEDOR FOREIGN KEY (vendedor_id) REFERENCES vendedor (id);

ALTER TABLE pedido_historico_status
    ADD CONSTRAINT fk_pedhissta_on_pedido FOREIGN KEY (pedido_id) REFERENCES pedido (id);

ALTER TABLE pedido_historico_status
    ADD CONSTRAINT fk_pedhissta_on_status_pedido FOREIGN KEY (status_pedido_id) REFERENCES status_pedido (id);

ALTER TABLE produto_tem_pedido
    ADD CONSTRAINT fk_protemped_on_pedido FOREIGN KEY (pedido_id) REFERENCES pedido (id);

ALTER TABLE produto_tem_pedido
    ADD CONSTRAINT fk_protemped_on_produto FOREIGN KEY (produto_id) REFERENCES produto (id);