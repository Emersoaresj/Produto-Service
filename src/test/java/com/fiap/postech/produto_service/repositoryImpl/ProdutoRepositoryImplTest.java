package com.fiap.postech.produto_service.repositoryImpl;

import com.fiap.postech.produto_service.api.dto.ProdutoDto;
import com.fiap.postech.produto_service.api.dto.ResponseDto;
import com.fiap.postech.produto_service.domain.exceptions.ErroInternoException;
import com.fiap.postech.produto_service.domain.model.Produto;
import com.fiap.postech.produto_service.gateway.database.ProdutoRepositoryImpl;
import com.fiap.postech.produto_service.gateway.database.entity.ProdutoEntity;
import com.fiap.postech.produto_service.gateway.database.repostory.ProdutoRepositoryJPA;
import com.fiap.postech.produto_service.api.mapper.ProdutoMapper;
import com.fiap.postech.produto_service.utils.ConstantUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoRepositoryImplTest {

    @InjectMocks
    private ProdutoRepositoryImpl produtoRepositoryImpl;

    @Mock
    private ProdutoRepositoryJPA produtoRepositoryJPA;

    // Mocks do Mapper (se necessário, se usar MapStruct pode mockar com Mockito)
    @Spy
    private ProdutoMapper produtoMapper = ProdutoMapper.INSTANCE;

    private Produto produto;
    private ProdutoEntity produtoEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produto = new Produto(
                1,
                "Produto Teste",
                "ABC-XYZ-123",
                new BigDecimal("100.00")
        );

        produtoEntity = new ProdutoEntity();
        produtoEntity.setIdProduto(1);
        produtoEntity.setNomeProduto("Produto Teste");
        produtoEntity.setSkuProduto("ABC-XYZ-123");
        produtoEntity.setPrecoProduto(new BigDecimal("100.00"));
    }

    @Test
    void testCadastrarProduto_sucesso() {
        when(produtoRepositoryJPA.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);

        ResponseDto response = produtoRepositoryImpl.cadastrarProduto(produto);

        assertEquals(ConstantUtils.PRODUTO_CADASTRADO, response.getMessage());

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertEquals(produtoEntity.getSkuProduto(), data.get("skuProduto"));
        assertEquals(produtoEntity.getNomeProduto(), data.get("nomeProduto"));
        assertEquals(produtoEntity.getPrecoProduto(), new BigDecimal(data.get("precoProduto").toString()));

        verify(produtoRepositoryJPA).save(any(ProdutoEntity.class));
    }

    @Test
    void testCadastrarProduto_erroBanco() {
        when(produtoRepositoryJPA.save(any(ProdutoEntity.class))).thenThrow(new RuntimeException("erro"));

        assertThrows(ErroInternoException.class, () -> produtoRepositoryImpl.cadastrarProduto(produto));
    }

    @Test
    void testFindBySkuProduto_sucesso() {
        when(produtoRepositoryJPA.findBySkuProduto(produto.getSkuProduto()))
                .thenReturn(Optional.of(produtoEntity));

        Optional<Produto> result = produtoRepositoryImpl.findBySkuProduto(produto.getSkuProduto());

        assertTrue(result.isPresent());
        assertEquals(produto.getSkuProduto(), result.get().getSkuProduto());
        assertEquals(produto.getNomeProduto(), result.get().getNomeProduto());
    }

    @Test
    void testFindBySkuProduto_naoEncontrado() {
        when(produtoRepositoryJPA.findBySkuProduto(anyString())).thenReturn(Optional.empty());

        Optional<Produto> result = produtoRepositoryImpl.findBySkuProduto("NAO-EXISTE");

        assertFalse(result.isPresent());
    }

    @Test
    void testFindBySkuProduto_erroBanco() {
        when(produtoRepositoryJPA.findBySkuProduto(anyString()))
                .thenThrow(new RuntimeException("erro"));

        assertThrows(ErroInternoException.class, () -> produtoRepositoryImpl.findBySkuProduto("SKU-ERRO"));
    }

    @Test
    void testListarTodos_sucesso() {
        List<ProdutoEntity> entities = List.of(produtoEntity);
        when(produtoRepositoryJPA.findAll()).thenReturn(entities);

        List<ProdutoDto> dtos = produtoRepositoryImpl.listarTodos();

        assertEquals(1, dtos.size());
        assertEquals(produtoEntity.getSkuProduto(), dtos.get(0).getSkuProduto());
    }

    @Test
    void testListarTodos_erroBanco() {
        when(produtoRepositoryJPA.findAll()).thenThrow(new RuntimeException("erro"));

        assertThrows(ErroInternoException.class, () -> produtoRepositoryImpl.listarTodos());
    }

    @Test
    void testAtualizarProduto_sucesso() {
        when(produtoRepositoryJPA.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);

        ResponseDto response = produtoRepositoryImpl.atualizarProduto(produto);

        assertEquals(ConstantUtils.PRODUTO_ATUALIZADO, response.getMessage());

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertEquals(produtoEntity.getSkuProduto(), data.get("skuProduto"));
        assertEquals(produtoEntity.getNomeProduto(), data.get("nomeProduto"));
        assertEquals(produtoEntity.getPrecoProduto(), new BigDecimal(data.get("precoProduto").toString()));

        verify(produtoRepositoryJPA).save(any(ProdutoEntity.class));
    }

    @Test
    void testAtualizarProduto_erroBanco() {
        when(produtoRepositoryJPA.save(any(ProdutoEntity.class))).thenThrow(new RuntimeException("erro"));

        assertThrows(ErroInternoException.class, () -> produtoRepositoryImpl.atualizarProduto(produto));
    }

    @Test
    void testDeletarProduto_sucesso() {
        doNothing().when(produtoRepositoryJPA).deleteBySkuProduto(produto.getSkuProduto());

        assertDoesNotThrow(() -> produtoRepositoryImpl.deletarProduto(produto.getSkuProduto()));

        verify(produtoRepositoryJPA).deleteBySkuProduto(produto.getSkuProduto());
    }

    @Test
    void testDeletarProduto_erroBanco() {
        doThrow(new RuntimeException("erro")).when(produtoRepositoryJPA).deleteBySkuProduto(anyString());

        assertThrows(ErroInternoException.class, () -> produtoRepositoryImpl.deletarProduto("SKU-ERRO"));
    }
}
