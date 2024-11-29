package org.service.estoque.controller;

import org.service.estoque.dto.ProdutoCreateDTO;
import org.service.estoque.dto.ProdutoQuantidadeDTO;
import org.service.estoque.dto.ProdutoResponseDTO;
import org.service.estoque.model.Produto;
import org.service.estoque.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<ProdutoResponseDTO> getProdutoById(@PathVariable Long produtoId) {
        Optional<Produto> produto = produtoService.findById(produtoId);
        if (produto.isPresent()) {
            ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(
                    produto.get().getId(),
                    produto.get().getNome(),
                    produto.get().getDescricao(),
                    produto.get().getQuantidade()
            );
            return ResponseEntity.ok(responseDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<ProdutoResponseDTO> getAllProdutos() {
        return produtoService.findAll().stream()
                .map(produto -> new ProdutoResponseDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getQuantidade()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping
    public Produto createProduto(@RequestBody ProdutoCreateDTO produtoDTO) {
        Produto novoProduto = new Produto();
        novoProduto.setNome(produtoDTO.getNome());
        novoProduto.setDescricao(produtoDTO.getDescricao());
        novoProduto.setQuantidade(produtoDTO.getQuantidade());
        return produtoService.save(novoProduto);
    }

    @PutMapping("/{id}/quantidade")
    public ResponseEntity<ProdutoResponseDTO> atualizarQuantidade(
            @PathVariable Long id, @RequestBody ProdutoQuantidadeDTO quantidadeDTO) {
        try {
            Produto produtoAtualizado = produtoService.atualizarQuantidade(id, quantidadeDTO.getQuantidade());

            ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(
                    produtoAtualizado.getId(),
                    produtoAtualizado.getNome(),
                    produtoAtualizado.getDescricao(),
                    produtoAtualizado.getQuantidade()
            );

            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        if (produtoService.findById(id).isPresent()) {
            produtoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/pedido/{id}")
    public Object consultarPedido(@PathVariable Long id) {
        return produtoService.buscarPedido(id);
    }
}
