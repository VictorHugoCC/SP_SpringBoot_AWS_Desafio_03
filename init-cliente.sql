CREATE ROLE cliente_user WITH LOGIN PASSWORD 'cliente_password';
ALTER ROLE cliente_user CREATEDB;
CREATE DATABASE cliente_db OWNER cliente_user;


CREATE TABLE cliente (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL
);
INSERT INTO cliente (nome, email) VALUES
    ('Victor Hugo', 'victor@example.com');
