package org.service.estoque.DTO;

import org.junit.jupiter.api.Test;
import org.service.estoque.dto.ProdutoResponseDTO;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoResponseDTOTest {

    @Test
    void testConstructorAndGetters() {
        Long id = 1L;
        String nome = "Produto Teste";
        String descricao = "Descrição do Produto";
        int quantidade = 100;

        ProdutoResponseDTO produtoResponseDTO = new ProdutoResponseDTO(id, nome, descricao, quantidade);

        assertEquals(id, produtoResponseDTO.getId());
        assertEquals(nome, produtoResponseDTO.getNome());
        assertEquals(descricao, produtoResponseDTO.getDescricao());
        assertEquals(quantidade, produtoResponseDTO.getQuantidade());
    }

    @Test
    void testSetters() {
        ProdutoResponseDTO produtoResponseDTO = new ProdutoResponseDTO(null, null, null, 0);

        Long id = 2L;
        String nome = "Outro Produto";
        String descricao = "Outra Descrição";
        int quantidade = 50;

        produtoResponseDTO.setId(id);
        produtoResponseDTO.setNome(nome);
        produtoResponseDTO.setDescricao(descricao);
        produtoResponseDTO.setQuantidade(quantidade);

        assertEquals(id, produtoResponseDTO.getId());
        assertEquals(nome, produtoResponseDTO.getNome());
        assertEquals(descricao, produtoResponseDTO.getDescricao());
        assertEquals(quantidade, produtoResponseDTO.getQuantidade());
    }
}
