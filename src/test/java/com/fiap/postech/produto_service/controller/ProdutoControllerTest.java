package com.fiap.postech.produto_service.controller;

import com.fiap.postech.produto_service.api.controller.ProdutoController;
import com.fiap.postech.produto_service.api.dto.*;
import com.fiap.postech.produto_service.domain.exceptions.ErroInternoException;
import com.fiap.postech.produto_service.domain.exceptions.internal.ProdutoNotFoundException;
import com.fiap.postech.produto_service.gateway.port.ProdutoServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ProdutoControllerTest {

    @InjectMocks
    private ProdutoController controller;

    @Mock
    private ProdutoServicePort service;

    private ProdutoRequest produtoRequest;
    private ProdutoAtualizaRequest atualizaRequest;
    private ProdutoDto produtoDto;

    @BeforeEach
    void setUp() {
        produtoRequest = new ProdutoRequest();
        produtoRequest.setNomeProduto("Produto Teste");
        produtoRequest.setSkuProduto("TESTE-001");
        produtoRequest.setPrecoProduto(BigDecimal.valueOf(100.0));

        atualizaRequest = new ProdutoAtualizaRequest();
        atualizaRequest.setNomeProduto("Produto Atualizado");
        atualizaRequest.setPrecoProduto(BigDecimal.valueOf(150.0));

        produtoDto = new ProdutoDto();
        produtoDto.setIdProduto(1);
        produtoDto.setNomeProduto("Produto Teste");
        produtoDto.setSkuProduto("TESTE-001");
        produtoDto.setPrecoProduto(BigDecimal.valueOf(100.0));
    }

    // --- POST ---
    @Test
    void deveCadastrarProdutoComSucesso() {
        ResponseDto response = new ResponseDto();
        response.setMessage("Produto cadastrado com sucesso!");
        when(service.cadastrarProduto(any(ProdutoRequest.class))).thenReturn(response);

        ResponseEntity<ResponseDto> result = controller.salvar(produtoRequest);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Produto cadastrado com sucesso!", result.getBody().getMessage());
    }

    // --- GET /sku/{sku} ---
    @Test
    void deveBuscarProdutoPorSkuComSucesso() {
        when(service.buscarPorSku("TESTE-001")).thenReturn(produtoDto);

        ResponseEntity<ProdutoDto> result = controller.buscarPorSku("TESTE-001");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("TESTE-001", result.getBody().getSkuProduto());
    }

    // --- GET (listar todos) ---
    @Test
    void deveListarTodosProdutosComSucesso() {
        List<ProdutoDto> lista = Arrays.asList(produtoDto);
        when(service.listarTodos()).thenReturn(lista);

        ResponseEntity<List<ProdutoDto>> result = controller.listarTodos();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals("TESTE-001", result.getBody().get(0).getSkuProduto());
    }

    // --- PUT /atualizar/{sku} ---
    @Test
    void deveAtualizarProdutoComSucesso() {
        ResponseDto response = new ResponseDto();
        response.setMessage("Produto atualizado com sucesso!");
        when(service.atualizarProduto(eq("TESTE-001"), any(ProdutoAtualizaRequest.class))).thenReturn(response);

        ResponseEntity<ResponseDto> result = controller.atualizarProduto("TESTE-001", atualizaRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Produto atualizado com sucesso!", result.getBody().getMessage());
    }

    // --- DELETE /deletar/{sku} ---
    @Test
    void deveDeletarProdutoComSucesso() {
        doNothing().when(service).deletarProduto("TESTE-001");

        ResponseEntity<Void> result = controller.deletarProduto("TESTE-001");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }

    // --- EXCEÇÃO: Produto não encontrado (exemplo) ---
    @Test
    void deveLancarProdutoNotFoundAoBuscar() {
        when(service.buscarPorSku("NAO-EXISTE")).thenThrow(new ProdutoNotFoundException("Produto não encontrado!"));

        assertThrows(ProdutoNotFoundException.class, () -> controller.buscarPorSku("NAO-EXISTE"));
    }

    @Test
    void deveLancarProdutoNotFoundAoAtualizar() {
        when(service.atualizarProduto(eq("NAO-EXISTE"), any(ProdutoAtualizaRequest.class)))
                .thenThrow(new ProdutoNotFoundException("Produto não encontrado!"));

        assertThrows(ProdutoNotFoundException.class, () -> controller.atualizarProduto("NAO-EXISTE", atualizaRequest));
    }

    @Test
    void deveLancarErroInternoAoCadastrar() {
        when(service.cadastrarProduto(any(ProdutoRequest.class))).thenThrow(new ErroInternoException("Erro interno!"));

        assertThrows(ErroInternoException.class, () -> controller.salvar(produtoRequest));
    }
}
