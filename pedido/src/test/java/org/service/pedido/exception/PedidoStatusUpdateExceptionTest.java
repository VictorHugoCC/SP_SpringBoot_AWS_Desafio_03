package org.service.pedido.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PedidoStatusUpdateExceptionTest {

    @Test
    void deveCriarPedidoStatusUpdateExceptionComMensagem() {
        PedidoStatusUpdateException exception = new PedidoStatusUpdateException("Erro ao atualizar status do pedido.");
        assertNotNull(exception);
        assertEquals("Erro ao atualizar status do pedido.", exception.getMessage());
    }
}
