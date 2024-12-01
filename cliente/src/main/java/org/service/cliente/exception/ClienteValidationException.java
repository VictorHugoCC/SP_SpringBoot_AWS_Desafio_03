package org.service.cliente.exception;

public class ClienteValidationException extends RuntimeException {
    public ClienteValidationException(String message) {
        super(message);
    }
}