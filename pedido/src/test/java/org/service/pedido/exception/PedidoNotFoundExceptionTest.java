package org.service.pedido.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PedidoNotFoundExceptionTest {

    @Test
    void deveCriarPedidoNotFoundExceptionComMensagem() {
        PedidoNotFoundException exception = new PedidoNotFoundException("Pedido não encontrado.");
        assertNotNull(exception);
        assertEquals("Pedido não encontrado.", exception.getMessage());
    }
}
