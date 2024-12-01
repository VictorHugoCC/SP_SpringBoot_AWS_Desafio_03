package org.service.pedido.DTO;

import org.junit.jupiter.api.Test;
import org.service.pedido.dto.ProdutoEstoqueDTO;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoEstoqueDTOTest {

    @Test
    void deveCriarProdutoEstoqueDTOCorretamente() {
        ProdutoEstoqueDTO produtoEstoqueDTO = new ProdutoEstoqueDTO(1L, "Produto 1", 10, 100.50);

        assertNotNull(produtoEstoqueDTO);
        assertEquals(1L, produtoEstoqueDTO.getId());
        assertEquals("Produto 1", produtoEstoqueDTO.getNome());
        assertEquals(10, produtoEstoqueDTO.getQuantidade());
        assertEquals(100.50, produtoEstoqueDTO.getPreco());
    }

    @Test
    void devePermitirAlterarQuantidade() {
        ProdutoEstoqueDTO produtoEstoqueDTO = new ProdutoEstoqueDTO(1L, "Produto 1", 5, 100.00);
        produtoEstoqueDTO.setQuantidade(15);

        assertEquals(15, produtoEstoqueDTO.getQuantidade());
    }

    @Test
    void devePermitirAlterarPreco() {
        ProdutoEstoqueDTO produtoEstoqueDTO = new ProdutoEstoqueDTO(1L, "Produto 1", 5, 200.00);
        produtoEstoqueDTO.setPreco(250.75);

        assertEquals(250.75, produtoEstoqueDTO.getPreco());
    }

    @Test
    void devePermitirAlterarNome() {
        ProdutoEstoqueDTO produtoEstoqueDTO = new ProdutoEstoqueDTO(1L, "Produto Antigo", 10, 100.00);
        produtoEstoqueDTO.setNome("Produto Novo");

        assertEquals("Produto Novo", produtoEstoqueDTO.getNome());
    }
}
