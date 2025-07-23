package com.fiap.postech.produto_service.api.controller;

import com.fiap.postech.produto_service.api.dto.ProdutoAtualizaRequest;
import com.fiap.postech.produto_service.api.dto.ProdutoDto;
import com.fiap.postech.produto_service.api.dto.ProdutoRequest;
import com.fiap.postech.produto_service.api.dto.ResponseDto;
import com.fiap.postech.produto_service.domain.exceptions.ErroInternoException;
import com.fiap.postech.produto_service.domain.exceptions.internal.ProdutoNotFoundException;
import com.fiap.postech.produto_service.gateway.port.ProdutoServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Gerenciamento de Produtos")
public class ProdutoController {

    @Autowired
    private ProdutoServicePort service;

    @Operation(summary = "Cadastrar um novo produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDto.class),
                    examples = @ExampleObject(value = "{\"mensagem\": \"Produto cadastrado com sucesso!\"}"))),

            @ApiResponse(
                    responseCode = "400", description = "Dados inválidos", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2024-07-22T20:56:01.564492",
                                    "status": 400,
                                    "errors": {
                                        "nomeProduto": "Nome obrigatório",
                                        "skuProduto": "SKU obrigatório",
                                        "precoProduto": "Preço obrigatório e deve ser maior que zero"
                                    }
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroInternoException.class),
                    examples = @ExampleObject(value = "{\"mensagem\": \"Erro interno!\"}")))})
    @PostMapping("/cadastrar")
    public ResponseEntity<ResponseDto> salvar(@Valid @RequestBody ProdutoRequest request) {
        ResponseDto produto = service.cadastrarProduto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @Operation(summary = "Buscar um produto pelo SKU")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProdutoDto.class),
                    examples = @ExampleObject(value = """
                                  {
                                     "idProduto": 1,
                                     "nomeProduto": "Apple Iphone 16 Pro",
                                     "skuProduto": "AP-IPH-001",
                                     "precoProduto": 8999.90
                                 }
                            """))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProdutoNotFoundException.class),
                    examples = @ExampleObject(value = "{\"mensagem\": \"Produto não encontrado!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroInternoException.class),
                    examples = @ExampleObject(value = "{\"mensagem\": \"Erro interno!\"}")))})
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProdutoDto> buscarPorSku(@PathVariable String sku) {
        ProdutoDto produto = service.buscarPorSku(sku);
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

    @Operation(summary = "Listar todos os Produtos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de produtos retornada com sucesso.",
                    content = @Content(
                            mediaType = "application/json", schema = @Schema(implementation = ProdutoDto.class),
                            examples = @ExampleObject(value = """
                                        [
                                            {
                                                "idProduto": 1,
                                                "nomeProduto": "Apple Iphone 16 Pro",
                                                "skuProduto": "AP-IPH-001",
                                                "precoProduto": 8999.90
                                            },
                                            {
                                                "idProduto": 2,
                                                "nomeProduto": "Apple Iphone 16",
                                                "skuProduto": "AP-IPH-002",
                                                "precoProduto": 10999.90
                                            }
                                        ]
                                    """))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProdutoNotFoundException.class),
                    examples = @ExampleObject(value = "{\"mensagem\": \"Produto não encontrado!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroInternoException.class),
                    examples = @ExampleObject(value = "{\"mensagem\": \"Erro interno!\"}")))})
    @GetMapping
    public ResponseEntity<List<ProdutoDto>> listarTodos() {
        List<ProdutoDto> produtos = service.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(produtos);
    }

    @Operation(summary = "Atualizar Produto pelo SKU")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso.", content = @Content(
                    schema = @Schema(implementation = ResponseDto.class),
                    examples = @ExampleObject(value = "{\"mensagem\": \"Produto atualizado com sucesso!\"}"))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProdutoNotFoundException.class),
                    examples = @ExampleObject(value = "{\"mensagem\": \"Produto não encontrado!\"}"))),
            @ApiResponse(
                    responseCode = "400", description = "Dados inválidos", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2024-07-22T20:56:01.564492",
                                    "status": 400,
                                    "errors": {
                                        "nomeProduto": "Nome obrigatório",
                                        "skuProduto": "SKU obrigatório"
                                    }
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroInternoException.class),
                    examples = @ExampleObject(value = "{\"mensagem\": \"Erro interno!\"}")))})
    @PutMapping("/atualizar/{sku}")
    public ResponseEntity<ResponseDto> atualizarProduto(@PathVariable String sku, @Valid @RequestBody ProdutoAtualizaRequest request) {
        ResponseDto response = service.atualizarProduto(sku, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Deletar Produto pelo SKU")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    @DeleteMapping("/deletar/{sku}")
    public ResponseEntity<Void> deletarProduto(@PathVariable String sku) {
        service.deletarProduto(sku);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
