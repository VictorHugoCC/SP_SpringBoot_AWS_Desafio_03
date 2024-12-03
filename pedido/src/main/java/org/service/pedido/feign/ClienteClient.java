package org.service.pedido.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cliente-service", url = "${cliente.service.url}")
public interface ClienteClient {
    @GetMapping("/clientes/{id}")
    ResponseEntity<Object> getClienteById(@PathVariable Long id);
}
