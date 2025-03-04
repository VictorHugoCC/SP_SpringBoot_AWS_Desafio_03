package org.service.cliente.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "pedido-service", url = "http://ec2-18-118-140-64.us-east-2.compute.amazonaws.com:8080/pedidos")
public interface PedidoClient {

    @GetMapping("/cliente/{clienteId}")
    List<Object> getPedidosByClienteId(@PathVariable("clienteId") Long clienteId);
}

