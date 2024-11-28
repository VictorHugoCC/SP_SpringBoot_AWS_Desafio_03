CREATE ROLE pedido_user WITH LOGIN PASSWORD 'pedido_password';
ALTER ROLE pedido_user CREATEDB;
CREATE DATABASE pedido_db OWNER pedido_user;

CREATE TABLE pedido (
                        id SERIAL PRIMARY KEY,
                        cliente_id INT NOT NULL,
                        data_pedido TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        total NUMERIC(10,2) NOT NULL,
                        status VARCHAR(20) NOT NULL
);

CREATE TABLE item_pedido (
                             id SERIAL PRIMARY KEY,
                             pedido_id INT NOT NULL,
                             produto_id INT NOT NULL,
                             quantidade INT NOT NULL,
                             preco_unitario NUMERIC(10,2) NOT NULL
);

INSERT INTO pedido (cliente_id, total, status) VALUES
    (1, 100.00, 'PROCESSANDO');

INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
    (1, 1, 2, 50.00);
