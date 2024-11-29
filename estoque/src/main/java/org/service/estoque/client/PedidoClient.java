package org.service.estoque.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pedido-service", url = "http://localhost:8082")
public interface PedidoClient {

    @GetMapping("/pedidos/{id}")
    Object buscarPedidoPorId(@PathVariable Long id);
}
