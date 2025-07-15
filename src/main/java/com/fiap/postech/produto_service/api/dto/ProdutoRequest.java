package com.fiap.postech.produto_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoRequest {

    @NotBlank(message = "Nome do produto é obrigatório")
    private String nomeProduto;

    @NotBlank(message = "SKU do produto é obrigatório")
    private String skuProduto;

    @NotNull(message = "Preço do produto é obrigatório")
    @Positive(message = "O preço deve ser um valor positivo")
    private BigDecimal precoProduto;
}
