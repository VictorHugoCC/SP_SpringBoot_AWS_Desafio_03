package org.service.estoque.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstoqueInsuficienteExceptionTest {

    @Test
    void deveRetornarMensagemDaExcecao() {
        EstoqueInsuficienteException exception = new EstoqueInsuficienteException("Estoque insuficiente.");
        assertEquals("Estoque insuficiente.", exception.getMessage());
    }
}
