package org.service.cliente.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pedido-service", url = "http://localhost:8083/pedidos")
public interface PedidoClient {
    @GetMapping("/{clienteId}")
    Object getPedidosByClienteId(@PathVariable Long clienteId);
}
