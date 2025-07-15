package com.fiap.postech.produto_service.gateway.port;

import com.fiap.postech.produto_service.api.dto.ProdutoDto;
import com.fiap.postech.produto_service.api.dto.ProdutoRequest;
import com.fiap.postech.produto_service.api.dto.ResponseDto;

public interface ProdutoServicePort {

    ResponseDto cadastrarProduto(ProdutoRequest request);

    ProdutoDto buscarPorSku(String sku);

}
