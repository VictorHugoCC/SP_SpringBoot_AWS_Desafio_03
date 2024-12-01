package org.service.estoque.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EstoqueNotFoundException.class)
    public ResponseEntity<String> handleEstoqueNotFoundException(EstoqueNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(QuantidadeInvalidaException.class)
    public ResponseEntity<String> handleQuantidadeInvalidaException(QuantidadeInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ProdutoEmPedidoException.class)
    public ResponseEntity<String> handleProdutoEmPedidoException(ProdutoEmPedidoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
