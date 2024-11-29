package org.service.estoque.controller;

import org.service.estoque.dto.ProdutoResponseDTO;
import org.service.estoque.model.Produto;
import org.service.estoque.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<Produto> getAllProdutos() {
        return produtoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        return produtoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Produto createProduto(@RequestBody Produto produto) {
        return produtoService.save(produto);
    }

    @PutMapping("/{id}/quantidade")
    public ResponseEntity<ProdutoResponseDTO> atualizarQuantidade(
            @PathVariable Long id, @RequestParam int quantidade) {
        Produto produtoAtualizado = produtoService.atualizarQuantidade(id, quantidade);
        return ResponseEntity.ok(new ProdutoResponseDTO(
                produtoAtualizado.getId(),
                produtoAtualizado.getNome(),
                produtoAtualizado.getDescricao(),
                produtoAtualizado.getQuantidade()
        ));
    }

    @GetMapping("/pedido/{id}")
    public Object consultarPedido(@PathVariable Long id) {
        return produtoService.buscarPedido(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        if (produtoService.findById(id).isPresent()) {
            produtoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
