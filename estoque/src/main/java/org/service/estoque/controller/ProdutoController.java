package org.service.estoque.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.service.estoque.dto.ProdutoCreateDTO;
import org.service.estoque.dto.ProdutoResponseDTO;
import org.service.estoque.model.Produto;
import org.service.estoque.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Endpoints para gerenciar produtos no estoque")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(summary = "Buscar produto por ID", description = "Busca os detalhes de um produto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
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

    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista com todos os produtos cadastrados no estoque")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
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

    @Operation(summary = "Criar um novo produto", description = "Cadastra um novo produto no estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> createProduto(@RequestBody ProdutoCreateDTO produtoDTO) {
        Produto novoProduto = new Produto();
        return getProdutoResponseDTOResponseEntity(produtoDTO, novoProduto);
    }

    private ResponseEntity<ProdutoResponseDTO> getProdutoResponseDTOResponseEntity(@RequestBody ProdutoCreateDTO produtoDTO, Produto novoProduto) {
        novoProduto.setNome(produtoDTO.getNome());
        novoProduto.setDescricao(produtoDTO.getDescricao());
        novoProduto.setQuantidade(produtoDTO.getQuantidade());

        Produto produtoSalvo = produtoService.save(novoProduto);

        return ResponseEntity.ok(new ProdutoResponseDTO(
                produtoSalvo.getId(),
                produtoSalvo.getNome(),
                produtoSalvo.getDescricao(),
                produtoSalvo.getQuantidade()
        ));
    }

    @Operation(summary = "Atualizar quantidade do produto", description = "Atualiza diminuindo a quantidade de um produto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{id}/quantidade")
    public ResponseEntity<ProdutoResponseDTO> atualizarQuantidade(
            @PathVariable Long id, @RequestParam int quantidade) {
        Produto produtoAtualizado = produtoService.atualizarQuantidade(id, quantidade);
        ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(
                produtoAtualizado.getId(),
                produtoAtualizado.getNome(),
                produtoAtualizado.getDescricao(),
                produtoAtualizado.getQuantidade()
        );
        return ResponseEntity.ok(responseDTO);
    }


    @Operation(summary = "Deletar produto", description = "Remove um produto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        if (produtoService.findById(id).isPresent()) {
            produtoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Incrementar quantidade do produto", description = "Incrementa a quantidade de um produto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade incrementada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PatchMapping("/{id}/quantidade/incrementar")
    public ResponseEntity<Produto> incrementarQuantidade(
            @PathVariable Long id,
            @RequestParam int quantidadeAdicionar) {
        Produto produtoAtualizado = produtoService.incrementarQuantidade(id, quantidadeAdicionar);
        return ResponseEntity.ok(produtoAtualizado);
    }
}
