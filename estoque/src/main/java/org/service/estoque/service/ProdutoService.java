package org.service.estoque.service;

import org.service.estoque.exception.QuantidadeInvalidaException;
import org.service.estoque.exception.EstoqueNotFoundException;
import org.service.estoque.exception.ProdutoEmPedidoException;
import org.service.estoque.feign.ProdutoClient;
import org.service.estoque.model.Produto;
import org.service.estoque.repository.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoClient produtoClient;

    public List<Produto> findAll() {
        logger.info("Buscando todos os produtos...");
        return produtoRepository.findAll();
    }

    public Optional<Produto> findById(Long id) {
        logger.info("Buscando produto com ID {}", id);
        return produtoRepository.findById(id);
    }

    public Produto save(Produto produto) {
        logger.info("Salvando produto: {}", produto.getNome());

        if (produto.getQuantidade() <= 0) {
            logger.error("Quantidade inválida para o produto: {}. Quantidade: {}", produto.getNome(), produto.getQuantidade());
            throw new QuantidadeInvalidaException("A quantidade do produto deve ser maior 0.");
        }

        return produtoRepository.save(produto);
    }

    public void deleteById(Long id) {
        logger.info("Verificando se o produto com ID {} está em algum pedido...", id);
        boolean isInPedido = Boolean.TRUE.equals(produtoClient.isProdutoInPedido(id).getBody());

        if (isInPedido) {
            logger.error("Não é possível excluir o produto com ID {} porque ele está em um pedido.", id);
            throw new ProdutoEmPedidoException("Produto com ID " + id + " está associado a um pedido e não pode ser excluído.");
        }

        logger.info("Deletando produto com ID {}", id);
        Produto produto = findProdutoOrThrow(id);
        produtoRepository.delete(produto);
    }

    public Produto atualizarQuantidade(Long id, int quantidadeSubtrair) {
        Produto produto = findProdutoOrThrow(id);

        if (quantidadeSubtrair <= 0) {
            throw new QuantidadeInvalidaException("A quantidade a ser subtraída deve ser maior que 0.");
        }

        int novaQuantidade = produto.getQuantidade() - quantidadeSubtrair;
        logger.info("Atualizando quantidade do produto com ID {}. Subtraindo: {}, nova quantia: {}", id, quantidadeSubtrair, novaQuantidade);

        if (novaQuantidade < 0) {
            throw new QuantidadeInvalidaException("Quantidade insuficiente no estoque para o produto com ID: " + id);
        }

        produto.setQuantidade(novaQuantidade);
        return produtoRepository.save(produto);
    }

    private Produto findProdutoOrThrow(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EstoqueNotFoundException("Produto com ID " + id + " não encontrado."));
    }
}
