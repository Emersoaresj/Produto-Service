package com.fiap.postech.produto_service.gateway.database;

import com.fiap.postech.produto_service.api.dto.ProdutoDto;
import com.fiap.postech.produto_service.domain.exceptions.ErroInternoException;
import com.fiap.postech.produto_service.domain.model.Produto;
import com.fiap.postech.produto_service.api.dto.ResponseDto;
import com.fiap.postech.produto_service.gateway.database.entity.ProdutoEntity;
import com.fiap.postech.produto_service.gateway.database.repostory.ProdutoRepositoryJPA;
import com.fiap.postech.produto_service.gateway.port.ProdutoRepositoryPort;
import com.fiap.postech.produto_service.api.mapper.ProdutoMapper;
import com.fiap.postech.produto_service.utils.ConstantUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class ProdutoRepositoryImpl implements ProdutoRepositoryPort {

    @Autowired
    private ProdutoRepositoryJPA produtoRepositoryJPA;

    @Transactional
    @Override
    public ResponseDto cadastrarProduto(Produto produto) {
        try {
            ProdutoEntity entity = ProdutoMapper.INSTANCE.domainToEntity(produto);
            ProdutoEntity savedEntity = produtoRepositoryJPA.save(entity);
            return montaResponse(savedEntity, "cadastro");
        } catch (Exception e) {
            log.error("Erro ao cadastrar produto", e);
            throw new ErroInternoException("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    @Override
    public Optional<Produto> findBySkuProduto(String sku) {
        try {
            return produtoRepositoryJPA.findBySkuProduto(sku)
                    .map(ProdutoMapper.INSTANCE::entityToDomain);
        } catch (Exception e) {
            log.error("Erro ao buscar produto por SKU", e);
            throw new ErroInternoException("Erro ao buscar produto: " + e.getMessage());
        }
    }


    @Override
    public List<ProdutoDto> listarTodos() {
        try {
            List<ProdutoEntity> produtos = produtoRepositoryJPA.findAll();
            return ProdutoMapper.INSTANCE.domainToDtoList(produtos);
        } catch (Exception e) {
            log.error("Erro ao buscar produtos", e);
            throw new ErroInternoException("Erro ao buscar produtos no banco de dados: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public ResponseDto atualizarProduto(Produto produto) {
        try {
            ProdutoEntity produtoEntity = ProdutoMapper.INSTANCE.domainToEntityUpdate(produto);
            produtoRepositoryJPA.save(produtoEntity);
            return montaResponse(produtoEntity, "update");
        } catch (Exception e) {
            log.error("Erro ao atualizar produto", e);
            throw new ErroInternoException("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deletarProduto(String sku) {
        try {
            produtoRepositoryJPA.deleteBySkuProduto(sku);
        } catch (Exception e) {
            log.error("Erro ao deletar produto", e);
            throw new ErroInternoException("Erro ao deletar produto: " + e.getMessage());
        }
    }

    private ResponseDto montaResponse(ProdutoEntity produtoEntity, String acao) {
        ResponseDto response = new ResponseDto();

        if("cadastro".equals(acao)) {
            response.setMessage(ConstantUtils.PRODUTO_CADASTRADO);
        } else {
            response.setMessage(ConstantUtils.PRODUTO_ATUALIZADO);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("skuProduto", produtoEntity.getSkuProduto());
        data.put("nomeProduto", produtoEntity.getNomeProduto());
        data.put("precoProduto", produtoEntity.getPrecoProduto());

        response.setData(data);
        return response;
    }
}
