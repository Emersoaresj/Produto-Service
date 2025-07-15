package com.fiap.postech.produto_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    private Integer idProduto;
    private String nomeProduto;
    private String skuProduto;
    private BigDecimal precoProduto;

    public boolean precoValido() {
        return precoProduto != null && precoProduto.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean skuValido() {
        if (skuProduto == null) {
            return false;
        }
        skuProduto = skuProduto.trim().toUpperCase().replace(" ", "-");
        return skuProduto.matches("^[A-Z0-9]{2,}-[A-Z0-9]{2,}-[0-9]{3,}$");
    }
}

