package org.service.pedido.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveRetornarRespostaParaClienteNotFoundException() {
        ClienteNotFoundException exception = new ClienteNotFoundException("Cliente não encontrado.");
        ResponseEntity<String> response = handler.handleClienteNotFoundException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cliente não encontrado.", response.getBody());
    }

    @Test
    void deveRetornarRespostaParaEstoqueInsuficienteException() {
        EstoqueInsuficienteException exception = new EstoqueInsuficienteException("Estoque insuficiente.");
        ResponseEntity<String> response = handler.handleEstoqueInsuficienteException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Estoque insuficiente.", response.getBody());
    }

    @Test
    void deveRetornarRespostaParaPedidoNotFoundException() {
        PedidoNotFoundException exception = new PedidoNotFoundException("Pedido não encontrado.");
        ResponseEntity<String> response = handler.handlePedidoNotFoundException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Pedido não encontrado.", response.getBody());
    }

    @Test
    void deveRetornarRespostaParaPedidoStatusUpdateException() {
        PedidoStatusUpdateException exception = new PedidoStatusUpdateException("Erro ao atualizar status do pedido.");
        ResponseEntity<String> response = handler.handlePedidoStatusUpdateException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao atualizar status do pedido.", response.getBody());
    }

    @Test
    void deveRetornarRespostaParaClienteIntegrationException() {
        ClienteIntegrationException exception = new ClienteIntegrationException("Erro na integração.", new RuntimeException("Erro interno"));
        ResponseEntity<String> response = handler.handleClienteIntegrationException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Erro ao integrar com o serviço de cliente: Erro na integração.", response.getBody());
    }

    @Test
    void deveRetornarRespostaParaExceptionGenerica() {
        Exception exception = new Exception("Erro interno inesperado.");
        ResponseEntity<String> response = handler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno no servidor: Erro interno inesperado.", response.getBody());
    }
}
