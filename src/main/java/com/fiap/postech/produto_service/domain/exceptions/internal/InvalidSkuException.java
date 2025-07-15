package com.fiap.postech.produto_service.domain.exceptions.internal;

public class InvalidSkuException extends RuntimeException {
    public InvalidSkuException(String message) {
        super(message);
    }
}
