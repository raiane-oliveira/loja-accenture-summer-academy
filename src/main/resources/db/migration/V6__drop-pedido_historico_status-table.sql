ALTER TABLE pedido_historico_status
DROP
FOREIGN KEY fk_pedhissta_on_pedido;

ALTER TABLE pedido_historico_status
DROP
FOREIGN KEY fk_pedhissta_on_status_pedido;

DROP TABLE pedido_historico_status;