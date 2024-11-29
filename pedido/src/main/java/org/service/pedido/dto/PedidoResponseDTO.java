package org.service.pedido.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PedidoResponseDTO {
    private Long id;
    private String cliente;
    private String status;
    private double total;
    private LocalDateTime data;
}