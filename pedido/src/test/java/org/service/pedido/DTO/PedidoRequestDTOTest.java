package org.service.pedido.DTO;

import org.junit.jupiter.api.Test;
import org.service.pedido.dto.PedidoRequestDTO;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PedidoRequestDTOTest {

    @Test
    void deveCriarPedidoRequestDTOCorretamente() {
        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(1L, Map.of(1L, 2, 2L, 5));

        assertNotNull(pedidoRequestDTO);
        assertEquals(1L, pedidoRequestDTO.getCliente());
        assertEquals(2, pedidoRequestDTO.getProdutos().get(1L));
        assertEquals(5, pedidoRequestDTO.getProdutos().get(2L));
    }

    @Test
    void devePermitirAlterarCliente() {
        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(1L, Map.of());
        pedidoRequestDTO.setCliente(2L);

        assertEquals(2L, pedidoRequestDTO.getCliente());
    }

    @Test
    void devePermitirAlterarProdutos() {
        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(1L, Map.of(1L, 2));
        pedidoRequestDTO.setProdutos(Map.of(3L, 7));

        assertEquals(7, pedidoRequestDTO.getProdutos().get(3L));
    }
}
