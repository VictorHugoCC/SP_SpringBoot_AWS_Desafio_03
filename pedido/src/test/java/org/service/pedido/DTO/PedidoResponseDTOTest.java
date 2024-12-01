package org.service.pedido.DTO;

import org.junit.jupiter.api.Test;
import org.service.pedido.dto.PedidoResponseDTO;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PedidoResponseDTOTest {

    @Test
    void deveCriarPedidoResponseDTOCorretamente() {
        PedidoResponseDTO pedidoResponseDTO = new PedidoResponseDTO(
                1L,
                "Cliente 1",
                Map.of(1L, 2, 2L, 5),
                "CONFIRMADO"
        );

        assertNotNull(pedidoResponseDTO);
        assertEquals(1L, pedidoResponseDTO.getId());
        assertEquals("Cliente 1", pedidoResponseDTO.getCliente());
        assertEquals(2, pedidoResponseDTO.getProdutos().get(1L));
        assertEquals(5, pedidoResponseDTO.getProdutos().get(2L));
        assertEquals("CONFIRMADO", pedidoResponseDTO.getStatus());
    }

    @Test
    void devePermitirAlterarStatus() {
        PedidoResponseDTO pedidoResponseDTO = new PedidoResponseDTO(1L, "Cliente 1", Map.of(), "PENDENTE");
        pedidoResponseDTO.setStatus("CANCELADO");

        assertEquals("CANCELADO", pedidoResponseDTO.getStatus());
    }

    @Test
    void devePermitirAlterarCliente() {
        PedidoResponseDTO pedidoResponseDTO = new PedidoResponseDTO(1L, "Cliente 1", Map.of(), "CONFIRMADO");
        pedidoResponseDTO.setCliente("Cliente 2");

        assertEquals("Cliente 2", pedidoResponseDTO.getCliente());
    }

    @Test
    void devePermitirAlterarProdutos() {
        PedidoResponseDTO pedidoResponseDTO = new PedidoResponseDTO(1L, "Cliente 1", Map.of(1L, 2), "CONFIRMADO");
        pedidoResponseDTO.setProdutos(Map.of(3L, 7));

        assertEquals(7, pedidoResponseDTO.getProdutos().get(3L));
    }
}
