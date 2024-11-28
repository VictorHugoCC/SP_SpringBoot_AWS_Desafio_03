package org.service.estoque.service;

import org.service.estoque.model.Produto;
import org.service.estoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }

    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    public void deleteById(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new IllegalArgumentException("Produto com ID " + id + " não encontrado.");
        }
        produtoRepository.deleteById(id);
    }

    public void reduzirEstoque(Long id, int quantidade) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Produto com ID " + id + " não encontrado."));
        if (produto.getQuantidade() < quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente para o produto " + produto.getNome());
        }
        produto.setQuantidade(produto.getQuantidade() - quantidade);
        produtoRepository.save(produto);
    }

    public void aumentarEstoque(Long id, int quantidade) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Produto com ID " + id + " não encontrado."));
        produto.setQuantidade(produto.getQuantidade() + quantidade);
        produtoRepository.save(produto);
    }
}
