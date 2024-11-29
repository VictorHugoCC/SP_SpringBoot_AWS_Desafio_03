package org.service.pedido.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoRequestDTO {
    private String cliente;
    private String status;
    private double total;
}