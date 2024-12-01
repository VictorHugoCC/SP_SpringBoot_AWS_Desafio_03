package org.service.estoque.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.service.estoque.exception.QuantidadeInvalidaException;
import org.service.estoque.exception.EstoqueNotFoundException;
import org.service.estoque.exception.ProdutoEmPedidoException;
import org.service.estoque.feign.ProdutoClient;
import org.service.estoque.model.Produto;
import org.service.estoque.repository.ProdutoRepository;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoClient produtoClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarTodosOsProdutos() {
        Produto produto1 = new Produto();
        Produto produto2 = new Produto();
        when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto1, produto2));

        List<Produto> produtos = produtoService.findAll();

        assertEquals(2, produtos.size());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarProdutoPorId() {
        Produto produto = new Produto();
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado = produtoService.findById(1L);

        assertTrue(resultado.isPresent());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        EstoqueNotFoundException exception = assertThrows(
                EstoqueNotFoundException.class,
                () -> produtoService.atualizarQuantidade(1L, 10)
        );

        assertEquals("Produto com ID 1 não encontrado.", exception.getMessage());
    }

    @Test
    void deveSalvarProdutoComSucesso() {
        Produto produto = new Produto();
        produto.setQuantidade(10);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto resultado = produtoService.save(produto);

        assertEquals(produto, resultado);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void deveLancarExcecaoAoSalvarProdutoComQuantidadeInvalida() {
        Produto produto = new Produto();
        produto.setQuantidade(0);

        QuantidadeInvalidaException exception = assertThrows(
                QuantidadeInvalidaException.class,
                () -> produtoService.save(produto)
        );

        assertEquals("A quantidade do produto deve ser maior 0.", exception.getMessage());
    }

    @Test
    void deveExcluirProdutoComSucesso() {
        Produto produto = new Produto();
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoClient.isProdutoInPedido(1L)).thenReturn(ResponseEntity.ok(false));

        produtoService.deleteById(1L);

        verify(produtoRepository, times(1)).delete(produto);
    }

    @Test
    void deveLancarExcecaoAoExcluirProdutoEmPedido() {
        when(produtoClient.isProdutoInPedido(1L)).thenReturn(ResponseEntity.ok(true));

        ProdutoEmPedidoException exception = assertThrows(
                ProdutoEmPedidoException.class,
                () -> produtoService.deleteById(1L)
        );

        assertEquals("Produto com ID 1 está associado a um pedido e não pode ser excluído.", exception.getMessage());
    }

    @Test
    void deveIncrementarQuantidadeComSucesso() {
        Produto produto = new Produto();
        produto.setQuantidade(10);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto resultado = produtoService.incrementarQuantidade(1L, 5);

        assertEquals(15, resultado.getQuantidade());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void deveLancarExcecaoAoIncrementarQuantidadeInvalida() {
        Produto produto = new Produto();
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        QuantidadeInvalidaException exception = assertThrows(
                QuantidadeInvalidaException.class,
                () -> produtoService.incrementarQuantidade(1L, 0)
        );

        assertEquals("A quantidade a ser adicionada deve ser maior que 0.", exception.getMessage());
    }

    @Test
    void deveAtualizarQuantidadeComSucesso() {
        Produto produto = new Produto();
        produto.setQuantidade(20);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto resultado = produtoService.atualizarQuantidade(1L, 10);

        assertEquals(10, resultado.getQuantidade());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeInsuficiente() {
        Produto produto = new Produto();
        produto.setQuantidade(5);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        QuantidadeInvalidaException exception = assertThrows(
                QuantidadeInvalidaException.class,
                () -> produtoService.atualizarQuantidade(1L, 10)
        );

        assertEquals("Quantidade insuficiente no estoque para o produto com ID: 1", exception.getMessage());
    }
}
