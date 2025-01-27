ALTER TABLE produto
    DROP FOREIGN KEY FK_PRODUTO_ON_ESTOQUE;

ALTER TABLE estoque
    ADD CONSTRAINT uc_estoque_produto UNIQUE (produto_id);

ALTER TABLE estoque
    ADD CONSTRAINT FK_ESTOQUE_ON_PRODUTO FOREIGN KEY (produto_id) REFERENCES produto (id);

ALTER TABLE pedido_historico_status
    ADD CONSTRAINT fk_pedhissta_on_pedido FOREIGN KEY (pedido_id) REFERENCES pedido (id);

ALTER TABLE pedido_historico_status
    ADD CONSTRAINT fk_pedhissta_on_status_pedido FOREIGN KEY (status_pedido_id) REFERENCES status_pedido (id);

ALTER TABLE produto_tem_pedido
    ADD CONSTRAINT fk_protemped_on_pedido FOREIGN KEY (pedido_id) REFERENCES pedido (id);

ALTER TABLE produto_tem_pedido
    ADD CONSTRAINT fk_protemped_on_produto FOREIGN KEY (produto_id) REFERENCES produto (id);

ALTER TABLE produto
    DROP COLUMN estoque_id;

ALTER TABLE produto
    MODIFY created_at datetime NULL;

ALTER TABLE pagamento
    MODIFY data_pagamento datetime NULL;

ALTER TABLE vendedor
    MODIFY email VARCHAR(100);

ALTER TABLE pedido
    MODIFY id BIGINT AUTO_INCREMENT;

ALTER TABLE pagamento
    DROP COLUMN metodo;

ALTER TABLE pagamento
    DROP COLUMN status;

ALTER TABLE pagamento
    ADD metodo VARCHAR(255) NOT NULL;

ALTER TABLE cliente
    MODIFY nome VARCHAR(50);

ALTER TABLE vendedor
    MODIFY nome VARCHAR(50);

ALTER TABLE vendedor
    MODIFY setor VARCHAR(50);

ALTER TABLE pagamento
    ADD status VARCHAR(255) NOT NULL;