package com.fiap.postech.produto_service.serviceImpl;


import com.fiap.postech.produto_service.api.dto.ProdutoAtualizaRequest;
import com.fiap.postech.produto_service.api.dto.ProdutoRequest;
import com.fiap.postech.produto_service.api.dto.ResponseDto;
import com.fiap.postech.produto_service.api.mapper.ProdutoMapper;
import com.fiap.postech.produto_service.domain.exceptions.ErroInternoException;
import com.fiap.postech.produto_service.domain.exceptions.internal.InvalidPrecoException;
import com.fiap.postech.produto_service.domain.exceptions.internal.InvalidSkuException;
import com.fiap.postech.produto_service.domain.exceptions.internal.ProdutoExistsException;
import com.fiap.postech.produto_service.domain.exceptions.internal.ProdutoNotFoundException;
import com.fiap.postech.produto_service.domain.model.Produto;
import com.fiap.postech.produto_service.gateway.port.ProdutoRepositoryPort;
import com.fiap.postech.produto_service.service.ProdutoServiceImpl;
import com.fiap.postech.produto_service.utils.ConstantUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceImplTest {

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @Mock
    private ProdutoRepositoryPort repositoryPort;

    // Produto padrão para os testes
    private Produto produtoPadrao;
    private ProdutoRequest produtoRequestPadrao;
    private ProdutoAtualizaRequest produtoAtualizaRequestPadrao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produtoPadrao = new Produto(
                1,
                "Notebook Dell Inspiron",
                "DL-NT-123",
                new BigDecimal("2999.90")
        );

        produtoRequestPadrao = new ProdutoRequest();
        produtoRequestPadrao.setNomeProduto(produtoPadrao.getNomeProduto());
        produtoRequestPadrao.setSkuProduto(produtoPadrao.getSkuProduto());
        produtoRequestPadrao.setPrecoProduto(produtoPadrao.getPrecoProduto());

        produtoAtualizaRequestPadrao = new ProdutoAtualizaRequest();
        produtoAtualizaRequestPadrao.setNomeProduto("Notebook Dell Novo");
        produtoAtualizaRequestPadrao.setPrecoProduto(new BigDecimal("3499.90"));
    }

    @Test
    void testCadastrarProduto_sucesso() {
        // Arrange
        when(repositoryPort.findBySkuProduto(produtoPadrao.getSkuProduto())).thenReturn(Optional.empty());

        ResponseDto respostaEsperada = new ResponseDto();
        respostaEsperada.setMessage(ConstantUtils.PRODUTO_CADASTRADO);
        Map<String, Object> data = new HashMap<>();
        data.put("skuProduto", produtoPadrao.getSkuProduto());
        data.put("nomeProduto", produtoPadrao.getNomeProduto());
        data.put("precoProduto", produtoPadrao.getPrecoProduto());
        respostaEsperada.setData(data);

        when(repositoryPort.cadastrarProduto(any())).thenReturn(respostaEsperada);

        // Act
        ResponseDto response = produtoService.cadastrarProduto(produtoRequestPadrao);

        // Assert
        assertEquals(ConstantUtils.PRODUTO_CADASTRADO, response.getMessage());

        @SuppressWarnings("unchecked")
        Map<String, Object> dataResponse = (Map<String, Object>) response.getData();
        assertEquals(produtoPadrao.getSkuProduto(), dataResponse.get("skuProduto"));
        assertEquals(produtoPadrao.getNomeProduto(), dataResponse.get("nomeProduto"));
        assertEquals(produtoPadrao.getPrecoProduto(), new BigDecimal(dataResponse.get("precoProduto").toString()));

        verify(repositoryPort).cadastrarProduto(any());
    }


    @Test
    void testCadastrarProduto_produtoExistente() {
        // Arrange
        when(repositoryPort.findBySkuProduto(produtoPadrao.getSkuProduto())).thenReturn(Optional.of(produtoPadrao));

        // Act & Assert
        assertThrows(ProdutoExistsException.class, () -> produtoService.cadastrarProduto(produtoRequestPadrao));
    }

    @Test
    void testCadastrarProduto_precoInvalido() {
        // Arrange
        produtoRequestPadrao.setPrecoProduto(BigDecimal.ZERO); // valor inválido

        // Act & Assert
        assertThrows(InvalidPrecoException.class, () -> produtoService.cadastrarProduto(produtoRequestPadrao));
    }

    @Test
    void testAtualizarProduto_sucesso() {
        // Arrange
        when(repositoryPort.findBySkuProduto(produtoPadrao.getSkuProduto())).thenReturn(Optional.of(produtoPadrao));

        ResponseDto respostaEsperada = new ResponseDto();
        respostaEsperada.setMessage(ConstantUtils.PRODUTO_ATUALIZADO);
        Map<String, Object> data = new HashMap<>();
        data.put("skuProduto", produtoPadrao.getSkuProduto());
        data.put("nomeProduto", produtoAtualizaRequestPadrao.getNomeProduto());
        data.put("precoProduto", produtoAtualizaRequestPadrao.getPrecoProduto());
        respostaEsperada.setData(data);

        when(repositoryPort.atualizarProduto(any())).thenReturn(respostaEsperada);

        // Act
        ResponseDto response = produtoService.atualizarProduto(produtoPadrao.getSkuProduto(), produtoAtualizaRequestPadrao);

        // Assert
        assertEquals(ConstantUtils.PRODUTO_ATUALIZADO, response.getMessage());

        @SuppressWarnings("unchecked")
        Map<String, Object> dataResponse = (Map<String, Object>) response.getData();
        assertEquals(produtoPadrao.getSkuProduto(), dataResponse.get("skuProduto"));
        assertEquals(produtoAtualizaRequestPadrao.getNomeProduto(), dataResponse.get("nomeProduto"));
        assertEquals(produtoAtualizaRequestPadrao.getPrecoProduto(), new BigDecimal(dataResponse.get("precoProduto").toString()));

        verify(repositoryPort).atualizarProduto(any());
    }

    @Test
    void testAtualizarProduto_produtoNaoEncontrado() {
        // Arrange
        when(repositoryPort.findBySkuProduto(produtoPadrao.getSkuProduto())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProdutoNotFoundException.class,
                () -> produtoService.atualizarProduto(produtoPadrao.getSkuProduto(), produtoAtualizaRequestPadrao));
    }

    @Test
    void testBuscarPorSku_sucesso() {
        when(repositoryPort.findBySkuProduto(produtoPadrao.getSkuProduto())).thenReturn(Optional.of(produtoPadrao));

        var result = produtoService.buscarPorSku(produtoPadrao.getSkuProduto());
        assertEquals(produtoPadrao.getSkuProduto(), result.getSkuProduto());
        assertEquals(produtoPadrao.getNomeProduto(), result.getNomeProduto());
    }

    @Test
    void testBuscarPorSku_naoEncontrado() {
        when(repositoryPort.findBySkuProduto(produtoPadrao.getSkuProduto())).thenReturn(Optional.empty());

        assertThrows(ProdutoNotFoundException.class,
                () -> produtoService.buscarPorSku(produtoPadrao.getSkuProduto()));
    }

    @Test
    void testListarTodos_sucesso() {
        var lista = List.of(
                ProdutoMapper.INSTANCE.domainToDtoClient(produtoPadrao)
        );
        when(repositoryPort.listarTodos()).thenReturn(lista);

        var result = produtoService.listarTodos();
        assertEquals(1, result.size());
        assertEquals(produtoPadrao.getSkuProduto(), result.get(0).getSkuProduto());
    }

    @Test
    void testDeletarProduto_sucesso() {
        when(repositoryPort.findBySkuProduto(produtoPadrao.getSkuProduto())).thenReturn(Optional.of(produtoPadrao));
        doNothing().when(repositoryPort).deletarProduto(produtoPadrao.getSkuProduto());

        assertDoesNotThrow(() -> produtoService.deletarProduto(produtoPadrao.getSkuProduto()));
        verify(repositoryPort).deletarProduto(produtoPadrao.getSkuProduto());
    }

    @Test
    void testDeletarProduto_naoEncontrado() {
        when(repositoryPort.findBySkuProduto(produtoPadrao.getSkuProduto())).thenReturn(Optional.empty());

        assertThrows(ProdutoNotFoundException.class,
                () -> produtoService.deletarProduto(produtoPadrao.getSkuProduto()));
    }

    @Test
    void testCadastrarProduto_erroInterno() {
        // Simular exception inesperada durante o cadastro (por exemplo, NullPointerException)
        when(repositoryPort.findBySkuProduto(produtoPadrao.getSkuProduto())).thenReturn(Optional.empty());
        when(repositoryPort.cadastrarProduto(any())).thenThrow(new NullPointerException("Unexpected error"));

        assertThrows(ErroInternoException.class, () -> produtoService.cadastrarProduto(produtoRequestPadrao));
    }

    @Test
    void testBuscarPorSku_erroInterno() {
        when(repositoryPort.findBySkuProduto(anyString())).thenThrow(new IllegalStateException("Falha inesperada"));

        assertThrows(ErroInternoException.class, () -> produtoService.buscarPorSku(produtoPadrao.getSkuProduto()));
    }

    @Test
    void testListarTodos_erroInterno() {
        when(repositoryPort.listarTodos()).thenThrow(new RuntimeException("Banco off"));

        assertThrows(ErroInternoException.class, () -> produtoService.listarTodos());
    }

    @Test
    void testAtualizarProduto_erroInterno() {
        when(repositoryPort.findBySkuProduto(produtoPadrao.getSkuProduto())).thenReturn(Optional.of(produtoPadrao));
        when(repositoryPort.atualizarProduto(any())).thenThrow(new RuntimeException("Falha inesperada"));

        assertThrows(ErroInternoException.class, () ->
                produtoService.atualizarProduto(produtoPadrao.getSkuProduto(), produtoAtualizaRequestPadrao));
    }

    @Test
    void testDeletarProduto_erroInterno() {
        when(repositoryPort.findBySkuProduto(produtoPadrao.getSkuProduto())).thenReturn(Optional.of(produtoPadrao));
        doThrow(new RuntimeException("Falha inesperada"))
                .when(repositoryPort).deletarProduto(produtoPadrao.getSkuProduto());

        assertThrows(ErroInternoException.class, () ->
                produtoService.deletarProduto(produtoPadrao.getSkuProduto()));
    }

    @Test
    void testCadastrarProduto_skuInvalido() {
        // Arrange: ProdutoRequest com SKU inválido (não segue padrão ^[A-Z0-9]{2,}-[A-Z0-9]{2,}-[0-9]{3,}$)
        ProdutoRequest produtoRequest = new ProdutoRequest();
        produtoRequest.setNomeProduto("Produto Teste");
        produtoRequest.setSkuProduto("123"); // SKU inválido!
        produtoRequest.setPrecoProduto(new BigDecimal("10.0"));

        // Act & Assert
        InvalidSkuException exception = assertThrows(
                InvalidSkuException.class,
                () -> produtoService.cadastrarProduto(produtoRequest)
        );
        assertEquals(ConstantUtils.SKU_INVALIDO, exception.getMessage());
    }

}
