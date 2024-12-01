package org.service.pedido.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteNotFoundExceptionTest {

    @Test
    void deveCriarClienteNotFoundExceptionComMensagem() {
        ClienteNotFoundException exception = new ClienteNotFoundException("Cliente não encontrado.");
        assertNotNull(exception);
        assertEquals("Cliente não encontrado.", exception.getMessage());
    }
}
