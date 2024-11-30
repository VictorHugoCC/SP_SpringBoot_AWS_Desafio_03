package org.service.pedido.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cliente-service", url = "http://localhost:8091/clientes")
public interface ClienteClient {

    @GetMapping("/{id}")
    ResponseEntity<Object> getClienteById(@PathVariable Long id);
}
