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
