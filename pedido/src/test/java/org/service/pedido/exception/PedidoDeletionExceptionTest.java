package org.service.pedido.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PedidoDeletionExceptionTest {

    @Test
    void deveCriarPedidoDeletionExceptionComMensagem() {
        PedidoDeletionException exception = new PedidoDeletionException("Erro ao deletar pedido.");
        assertNotNull(exception);
        assertEquals("Erro ao deletar pedido.", exception.getMessage());
    }
}
