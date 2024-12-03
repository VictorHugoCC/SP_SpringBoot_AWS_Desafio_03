package org.service.estoque.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pedido-service", url = "http://ec2-18-118-140-64.us-east-2.compute.amazonaws.com:8090/pedidos")
public interface ProdutoClient {

    @GetMapping("/produto")
    ResponseEntity<Boolean> isProdutoInPedido(@RequestParam("produtoId") Long produtoId);
}
