package org.service.pedido.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PedidoRequestDTO {

    @NotNull
    private Long cliente; // Altere aqui de String para Long

    @NotNull
    private Map<Long, Integer> produtos; // ID do produto e quantidade
}
