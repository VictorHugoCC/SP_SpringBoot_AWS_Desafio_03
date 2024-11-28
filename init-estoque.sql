CREATE ROLE estoque_user WITH LOGIN PASSWORD 'estoque_password';
ALTER ROLE estoque_user CREATEDB;
CREATE DATABASE estoque_db OWNER estoque_user;


CREATE TABLE produto (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         descricao TEXT,
                         preco NUMERIC(10,2) NOT NULL,
                         quantidade INT NOT NULL
);

INSERT INTO produto (nome, descricao, preco, quantidade) VALUES
                                                             ('Produto A', 'Descrição do Produto A', 10.00, 100),
                                                             ('Produto B', 'Descrição do Produto B', 20.00, 200);
