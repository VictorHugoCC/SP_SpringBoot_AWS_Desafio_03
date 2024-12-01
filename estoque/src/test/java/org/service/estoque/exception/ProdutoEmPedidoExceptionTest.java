package org.service.estoque.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoEmPedidoExceptionTest {

    @Test
    void deveRetornarMensagemDaExcecao() {
        ProdutoEmPedidoException exception = new ProdutoEmPedidoException("Produto está associado a um pedido.");
        assertEquals("Produto está associado a um pedido.", exception.getMessage());
    }
}
