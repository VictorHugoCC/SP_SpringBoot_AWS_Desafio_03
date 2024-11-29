package org.service.pedido.feign;

import org.service.pedido.dto.ProdutoEstoqueDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "estoque-service", url = "http://localhost:8092")
public interface EstoqueClient {

    @GetMapping("/estoque/{produtoId}")
    ProdutoEstoqueDTO getProdutoById(@PathVariable("produtoId") Long produtoId);

    @PutMapping("/estoque/{produtoId}")
    void atualizarQuantidade(@PathVariable("produtoId") Long produtoId, @RequestParam int quantidade);
}

