package org.service.pedido.exception;

public class ClienteIntegrationException extends RuntimeException {
    public ClienteIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
