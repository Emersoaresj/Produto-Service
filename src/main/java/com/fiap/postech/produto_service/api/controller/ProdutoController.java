package com.fiap.postech.produto_service.api.controller;

import com.fiap.postech.produto_service.api.dto.ProdutoDto;
import com.fiap.postech.produto_service.api.dto.ProdutoRequest;
import com.fiap.postech.produto_service.api.dto.ResponseDto;
import com.fiap.postech.produto_service.gateway.port.ProdutoServicePort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoServicePort service;

    @PostMapping
    public ResponseEntity<ResponseDto> salvar(@Valid @RequestBody ProdutoRequest request) {
        ResponseDto produto = service.cadastrarProduto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProdutoDto> buscarPorSku(@PathVariable String sku) {
        ProdutoDto produto = service.buscarPorSku(sku);
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

}
