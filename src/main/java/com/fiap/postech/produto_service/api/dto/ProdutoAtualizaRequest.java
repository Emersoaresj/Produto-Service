package com.fiap.postech.produto_service.api.dto;

import java.math.BigDecimal;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "DTO para atualização de produto.")
public class ProdutoAtualizaRequest {

    @Schema(description = "Nome do produto", example = "Notebook Dell Inspiron")
    private String nomeProduto;

    @Schema(description = "Preço do produto", example = "2999.90")
    private BigDecimal precoProduto;

}
