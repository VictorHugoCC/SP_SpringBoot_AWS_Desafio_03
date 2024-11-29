package org.service.pedido.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PedidoRequestDTO {
    private String cliente;
    private Map<Long, Integer> produtos;
}
