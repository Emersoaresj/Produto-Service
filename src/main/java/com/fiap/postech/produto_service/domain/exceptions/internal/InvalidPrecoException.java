package com.fiap.postech.produto_service.domain.exceptions.internal;

public class InvalidPrecoException extends RuntimeException {
    public InvalidPrecoException(String message) {
        super(message);
    }
}
