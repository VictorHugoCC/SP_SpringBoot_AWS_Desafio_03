package org.service.pedido.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstoqueInsuficienteExceptionTest {

    @Test
    void deveCriarEstoqueInsuficienteExceptionComMensagem() {
        EstoqueInsuficienteException exception = new EstoqueInsuficienteException("Estoque insuficiente.");
        assertNotNull(exception);
        assertEquals("Estoque insuficiente.", exception.getMessage());
    }
}
