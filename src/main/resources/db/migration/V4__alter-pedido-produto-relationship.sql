CREATE TABLE pedido_tem_produtos
(
    quantidade_produto INT DEFAULT 1     NOT NULL,
    pedido_id          BIGINT            NOT NULL,
    produto_id         BIGINT            NOT NULL,
    CONSTRAINT pk_pedido_tem_produtos PRIMARY KEY (pedido_id, produto_id)
);

ALTER TABLE pedido_tem_produtos
    ADD CONSTRAINT FK_PEDIDO_TEM_PRODUTOS_ON_PEDIDO FOREIGN KEY (pedido_id) REFERENCES pedido (id);

ALTER TABLE pedido_tem_produtos
    ADD CONSTRAINT FK_PEDIDO_TEM_PRODUTOS_ON_PRODUTO FOREIGN KEY (produto_id) REFERENCES produto (id);

DROP TABLE produto_tem_pedido;

ALTER TABLE produto
    MODIFY created_at datetime NULL;

ALTER TABLE pedido
    MODIFY id BIGINT AUTO_INCREMENT;