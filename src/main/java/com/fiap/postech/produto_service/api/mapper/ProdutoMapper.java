package com.fiap.postech.produto_service.api.mapper;

import com.fiap.postech.produto_service.domain.model.Produto;
import com.fiap.postech.produto_service.api.dto.ProdutoDto;
import com.fiap.postech.produto_service.api.dto.ProdutoRequest;
import com.fiap.postech.produto_service.gateway.database.entity.ProdutoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProdutoMapper {

    ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);

    @Mapping(target = "idProduto", ignore = true)
    Produto requestToDomain(ProdutoRequest request);

    @Mapping(target = "idProduto", ignore = true)
    ProdutoEntity domainToEntity(Produto produto);

    Produto entityToDomain(ProdutoEntity entity);

    ProdutoDto domainToDtoClient (Produto produto);
}
