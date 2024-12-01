package org.service.estoque.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantidadeInvalidaExceptionTest {

    @Test
    void deveRetornarMensagemDaExcecao() {
        QuantidadeInvalidaException exception = new QuantidadeInvalidaException("Quantidade inválida.");
        assertEquals("Quantidade inválida.", exception.getMessage());
    }
}
