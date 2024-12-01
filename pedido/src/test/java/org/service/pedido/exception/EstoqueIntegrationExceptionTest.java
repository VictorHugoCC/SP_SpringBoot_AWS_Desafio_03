package org.service.pedido.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstoqueIntegrationExceptionTest {

    @Test
    void deveCriarEstoqueIntegrationExceptionComMensagemECausa() {
        Throwable cause = new RuntimeException("Erro de comunicação com estoque");
        EstoqueIntegrationException exception = new EstoqueIntegrationException("Erro ao integrar com o serviço de estoque.", cause);

        assertNotNull(exception);
        assertEquals("Erro ao integrar com o serviço de estoque.", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
