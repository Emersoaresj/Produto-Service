package com.fiap.postech.produto_service.gateway.database.repostory;

import com.fiap.postech.produto_service.gateway.database.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepositoryJPA extends JpaRepository<ProdutoEntity, Integer> {

    Optional<ProdutoEntity> findBySkuProduto(String sku);

    void deleteBySkuProduto(String sku);
}
