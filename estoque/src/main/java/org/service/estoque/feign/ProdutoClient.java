package org.service.estoque.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "produto-service", url = "http://localhost:8090/pedidos")
public interface ProdutoClient {

    @GetMapping("/produto")
    ResponseEntity<Boolean> isProdutoInPedido(@RequestParam Long produtoId);
}
