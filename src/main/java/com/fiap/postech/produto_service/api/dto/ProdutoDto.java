package com.fiap.postech.produto_service.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "DTO de retorno para dados do produto.")
public class ProdutoDto {

    @Schema(description = "ID interno do produto", example = "1")
    private Integer idProduto;

    @Schema(description = "Nome do produto", example = "Apple iPhone 16 Pro")
    private String nomeProduto;

    @Schema(description = "SKU do produto (código único)", example = "AP-IPH-001")
    private String skuProduto;

    @Schema(description = "Preço do produto", example = "8999.90")
    private BigDecimal precoProduto;
}

