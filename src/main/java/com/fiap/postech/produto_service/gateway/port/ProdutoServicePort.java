package com.fiap.postech.produto_service.gateway.port;

import com.fiap.postech.produto_service.api.dto.ProdutoAtualizaRequest;
import com.fiap.postech.produto_service.api.dto.ProdutoDto;
import com.fiap.postech.produto_service.api.dto.ProdutoRequest;
import com.fiap.postech.produto_service.api.dto.ResponseDto;

import java.util.List;

public interface ProdutoServicePort {

    ResponseDto cadastrarProduto(ProdutoRequest request);

    ProdutoDto buscarPorSku(String sku);

    List<ProdutoDto> listarTodos();

    ResponseDto atualizarProduto(String sku, ProdutoAtualizaRequest request);

    void deletarProduto(String sku);

}
