package com.fiap.postech.produto_service.gateway.port;

import com.fiap.postech.produto_service.api.dto.ProdutoDto;
import com.fiap.postech.produto_service.domain.model.Produto;
import com.fiap.postech.produto_service.api.dto.ResponseDto;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepositoryPort {

    ResponseDto cadastrarProduto(Produto produto);

    Optional<Produto> findBySkuProduto(String sku);

    List<ProdutoDto> listarTodos();

    ResponseDto atualizarProduto(Produto produto);

    void deletarProduto(String sku);
}

