package com.fiap.postech.produto_service.gateway.port;

import com.fiap.postech.produto_service.domain.model.Produto;
import com.fiap.postech.produto_service.api.dto.ResponseDto;

import java.util.Optional;

public interface ProdutoRepositoryPort {

    ResponseDto cadastrarProduto(Produto produto);

    Optional<Produto> findBySkuProduto(String sku);
}

