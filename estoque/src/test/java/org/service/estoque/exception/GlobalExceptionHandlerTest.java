package org.service.estoque.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void deveRetornarRespostaParaEstoqueNotFoundException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        EstoqueNotFoundException exception = new EstoqueNotFoundException("Produto não encontrado.");
        ResponseEntity<String> response = handler.handleEstoqueNotFoundException(exception);
        assertEquals("Produto não encontrado.", response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void deveRetornarRespostaParaQuantidadeInvalidaException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        QuantidadeInvalidaException exception = new QuantidadeInvalidaException("Quantidade inválida.");
        ResponseEntity<String> response = handler.handleQuantidadeInvalidaException(exception);
        assertEquals("Quantidade inválida.", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void deveRetornarRespostaParaProdutoEmPedidoException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ProdutoEmPedidoException exception = new ProdutoEmPedidoException("Produto está associado a um pedido.");
        ResponseEntity<String> response = handler.handleProdutoEmPedidoException(exception);
        assertEquals("Produto está associado a um pedido.", response.getBody());
        assertEquals(409, response.getStatusCodeValue());
    }
}
