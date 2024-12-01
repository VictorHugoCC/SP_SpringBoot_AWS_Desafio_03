package org.service.estoque.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstoqueNotFoundExceptionTest {

    @Test
    void deveRetornarMensagemDaExcecao() {
        EstoqueNotFoundException exception = new EstoqueNotFoundException("Produto não encontrado.");
        assertEquals("Produto não encontrado.", exception.getMessage());
    }
}
