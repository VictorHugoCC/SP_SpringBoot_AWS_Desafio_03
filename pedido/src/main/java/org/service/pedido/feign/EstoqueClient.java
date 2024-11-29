package org.service.pedido.feign;

import org.service.pedido.dto.ProdutoEstoqueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "estoque-client", url = "http://localhost:8092")
public interface EstoqueClient {

    Logger logger = LoggerFactory.getLogger(EstoqueClient.class);

    @GetMapping("/estoque/{produtoId}")
    default ProdutoEstoqueDTO getProdutoById(@PathVariable Long produtoId) {
        logger.info("Enviando requisição GET para buscar produto ID: {}", produtoId);
        return null;
    }

    @PutMapping("/estoque/{produtoId}")
    default void atualizarQuantidade(@PathVariable Long produtoId, @RequestParam int quantidade) {
        logger.info("Enviando requisição PUT para atualizar o produto ID: {} com quantidade: {}", produtoId, quantidade);
    }
}
