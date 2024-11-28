package org.service.cliente.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pedido-service", url = "http://pedido:8083")
public interface PedidoClient {

    @GetMapping("/pedidos/cliente/{clienteId}")
    Object getPedidosByClienteId(@PathVariable Long clienteId);
}
