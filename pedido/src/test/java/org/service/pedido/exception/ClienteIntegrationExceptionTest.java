package org.service.pedido.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteIntegrationExceptionTest {

    @Test
    void deveCriarClienteIntegrationExceptionComMensagemECausa() {
        Throwable cause = new RuntimeException("Erro de rede");
        ClienteIntegrationException exception = new ClienteIntegrationException("Erro ao integrar com o cliente.", cause);

        assertNotNull(exception);
        assertEquals("Erro ao integrar com o cliente.", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
