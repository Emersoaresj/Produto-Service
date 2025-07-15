package com.fiap.postech.produto_service.domain.exceptions.internal;

public class ProdutoExistsException extends RuntimeException {
    public ProdutoExistsException(String message) {
        super(message);
    }
}
