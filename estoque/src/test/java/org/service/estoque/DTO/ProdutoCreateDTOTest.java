package org.service.estoque.DTO;

import org.junit.jupiter.api.Test;
import org.service.estoque.dto.ProdutoCreateDTO;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoCreateDTOTest {

    @Test
    void testGettersAndSetters() {
        ProdutoCreateDTO produtoCreateDTO = new ProdutoCreateDTO();

        String nome = "Produto Teste";
        String descricao = "Descrição do Produto";
        int quantidade = 100;

        produtoCreateDTO.setNome(nome);
        produtoCreateDTO.setDescricao(descricao);
        produtoCreateDTO.setQuantidade(quantidade);

        assertEquals(nome, produtoCreateDTO.getNome());
        assertEquals(descricao, produtoCreateDTO.getDescricao());
        assertEquals(quantidade, produtoCreateDTO.getQuantidade());
    }

    @Test
    void testDefaultConstructor() {
        ProdutoCreateDTO produtoCreateDTO = new ProdutoCreateDTO();

        assertNull(produtoCreateDTO.getNome());
        assertNull(produtoCreateDTO.getDescricao());
        assertEquals(0, produtoCreateDTO.getQuantidade());
    }
}
