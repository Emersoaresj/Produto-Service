package com.fiap.postech.produto_service.utils;

import lombok.Data;

@Data
public class ConstantUtils {




    private ConstantUtils() {
        throw new IllegalStateException("Classe Utilitária");
    }


    //ERROS
    public static final String PRODUTO_JA_EXISTE = "O produto já está cadastrado!";
    public static final String PRECO_INVALIDO = "Preço inválido! O preço deve ser maior que zero.";
    public static final String SKU_INVALIDO = "SKU inválido. O SKU deve ser alfanumérico e com hífens entre as palavras. Exemplo válido: NB-DEL-001";
    public static final String PRODUTO_NAO_ENCONTRADO = "Produto não encontrado para o SKU informado.";

    //SUCESSO
    public static final String PRODUTO_CADASTRADO = "Produto cadastrado com sucesso!";
    public static final String PRODUTO_ATUALIZADO = "Produto atualizado com sucesso!";
}
