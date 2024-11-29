package org.service.pedido.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class PedidoResponseDTO {

    private Long id;
    private String cliente;
    private Map<Long, Integer> produtos;
    private String status;

    public PedidoResponseDTO(Long id, String cliente, Map<Long, Integer> produtos, String status) {
        this.id = id;
        this.cliente = cliente;
        this.produtos = produtos;
        this.status = status;
    }
}
