ALTER TABLE status_pedido
    ADD pedido_id BIGINT NULL;

ALTER TABLE status_pedido
    ADD status VARCHAR(255) NULL;

ALTER TABLE status_pedido
    MODIFY pedido_id BIGINT NOT NULL;

ALTER TABLE status_pedido
    MODIFY status VARCHAR (255) NOT NULL;

ALTER TABLE status_pedido
    ADD CONSTRAINT FK_STATUS_PEDIDO_ON_PEDIDO FOREIGN KEY (pedido_id) REFERENCES pedido (id);

ALTER TABLE status_pedido
DROP
COLUMN descricao;

ALTER TABLE vendedor
    MODIFY email VARCHAR (100) NOT NULL;

ALTER TABLE cliente
    MODIFY nome VARCHAR (50) NOT NULL;

ALTER TABLE vendedor
    MODIFY nome VARCHAR (50) NOT NULL;

ALTER TABLE estoque
    MODIFY produto_id BIGINT NOT NULL;

ALTER TABLE vendedor
    MODIFY setor VARCHAR (50) NOT NULL;