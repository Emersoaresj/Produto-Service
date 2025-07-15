package com.fiap.postech.produto_service.service;

import com.fiap.postech.produto_service.domain.model.Produto;
import com.fiap.postech.produto_service.domain.exceptions.*;
import com.fiap.postech.produto_service.domain.exceptions.internal.InvalidPrecoException;
import com.fiap.postech.produto_service.domain.exceptions.internal.InvalidSkuException;
import com.fiap.postech.produto_service.domain.exceptions.internal.ProdutoExistsException;
import com.fiap.postech.produto_service.domain.exceptions.internal.ProdutoNotFoundException;
import com.fiap.postech.produto_service.api.dto.ProdutoDto;
import com.fiap.postech.produto_service.api.dto.ProdutoRequest;
import com.fiap.postech.produto_service.api.dto.ResponseDto;
import com.fiap.postech.produto_service.gateway.port.ProdutoServicePort;
import com.fiap.postech.produto_service.gateway.port.ProdutoRepositoryPort;
import com.fiap.postech.produto_service.api.mapper.ProdutoMapper;
import com.fiap.postech.produto_service.utils.ConstantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProdutoServiceImpl implements ProdutoServicePort {

    @Autowired
    private ProdutoRepositoryPort repositoryPort;

    @Override
    public ResponseDto cadastrarProduto(ProdutoRequest request) {

        try {
            Produto produto = ProdutoMapper.INSTANCE.requestToDomain(request);

            validaProduto(produto);

            return repositoryPort.cadastrarProduto(produto);
        } catch (InvalidPrecoException | InvalidSkuException | ProdutoExistsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao cadastrar produto", e);
            throw new ErroInternoException("Erro interno ao tentar cadastrar produto: " + e.getMessage());
        }


    }

    @Override
    public ProdutoDto buscarPorSku(String sku) {
        try {
            Produto produto = repositoryPort.findBySkuProduto(sku)
                    .orElseThrow(() -> new ProdutoNotFoundException(ConstantUtils.PRODUTO_NAO_ENCONTRADO));
            return ProdutoMapper.INSTANCE.domainToDtoClient(produto);
        } catch (ProdutoNotFoundException e) {
            log.error("Produto não encontrado para o SKU: {}", sku, e);
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar produto por SKU: {}", sku, e);
            throw new ErroInternoException("Erro interno ao tentar buscar produto: " + e.getMessage());
        }
    }


    private void validaProduto(Produto produto) {
        if (!produto.precoValido()) {
            log.warn("Preço inválido para produto SKU {}: {}", produto.getSkuProduto(), produto.getPrecoProduto());
            throw new InvalidPrecoException(ConstantUtils.PRECO_INVALIDO);
        }

        if (!produto.skuValido()) {
            log.warn("SKU inválido: {}", produto.getSkuProduto());
            throw new InvalidSkuException(ConstantUtils.SKU_INVALIDO);
        }

        if (repositoryPort.findBySkuProduto(produto.getSkuProduto()).isPresent()) {
            log.warn("Produto já cadastrado com SKU: {}", produto.getSkuProduto());
            throw new ProdutoExistsException(ConstantUtils.PRODUTO_JA_EXISTE);
        }
    }
}
