package org.service.estoque.controller;

import org.service.estoque.dto.ProdutoCreateDTO;
import org.service.estoque.dto.ProdutoQuantidadeDTO;
import org.service.estoque.dto.ProdutoResponseDTO;
import org.service.estoque.model.Produto;
import org.service.estoque.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<ProdutoResponseDTO> getAllProdutos() {
        return produtoService.findAll().stream()
                .map(produto -> new ProdutoResponseDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getQuantidade(),
                        produto.getPreco()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> getProdutoById(@PathVariable Long id) {
        return produtoService.findById(id)
                .map(produto -> ResponseEntity.ok(new ProdutoResponseDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getQuantidade(),
                        produto.getPreco()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> createProduto(@RequestBody ProdutoCreateDTO produtoDTO) {
        Produto novoProduto = new Produto();
        return getProdutoResponseDTOResponseEntity(produtoDTO, novoProduto);
    }

    private ResponseEntity<ProdutoResponseDTO> getProdutoResponseDTOResponseEntity(@RequestBody ProdutoCreateDTO produtoDTO, Produto novoProduto) {
        novoProduto.setNome(produtoDTO.getNome());
        novoProduto.setDescricao(produtoDTO.getDescricao());
        novoProduto.setQuantidade(produtoDTO.getQuantidade());
        novoProduto.setPreco(produtoDTO.getPreco());

        Produto produtoSalvo = produtoService.save(novoProduto);

        return ResponseEntity.ok(new ProdutoResponseDTO(
                produtoSalvo.getId(),
                produtoSalvo.getNome(),
                produtoSalvo.getDescricao(),
                produtoSalvo.getQuantidade(),
                produtoSalvo.getPreco()
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> updateProduto(
            @PathVariable Long id, @RequestBody ProdutoCreateDTO produtoDTO) {
        return produtoService.findById(id)
                .map(produtoExistente -> {
                    return getProdutoResponseDTOResponseEntity(produtoDTO, produtoExistente);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        if (produtoService.findById(id).isPresent()) {
            produtoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/quantidade")
    public ResponseEntity<ProdutoResponseDTO> atualizarQuantidade(
            @PathVariable Long id, @RequestBody ProdutoQuantidadeDTO quantidadeDTO) {
        try {
            Produto produtoAtualizado = produtoService.atualizarQuantidade(id, quantidadeDTO.getQuantidade());

            ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(
                    produtoAtualizado.getId(),
                    produtoAtualizado.getNome(),
                    produtoAtualizado.getDescricao(),
                    produtoAtualizado.getQuantidade(),
                    produtoAtualizado.getPreco()
            );

            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
