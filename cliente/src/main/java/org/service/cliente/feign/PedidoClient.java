package org.service.cliente.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "pedido-service", url = "http://localhost:8090/pedidos")
public interface PedidoClient {

    @GetMapping("/cliente/{clienteId}")
    List<Object> getPedidosByClienteId(@PathVariable("clienteId") Long clienteId);
}

