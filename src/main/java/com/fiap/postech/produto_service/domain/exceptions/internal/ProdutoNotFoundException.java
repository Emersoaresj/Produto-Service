package com.fiap.postech.produto_service.domain.exceptions.internal;

public class ProdutoNotFoundException extends RuntimeException {
    public ProdutoNotFoundException(String message) {
        super(message);
    }
}
