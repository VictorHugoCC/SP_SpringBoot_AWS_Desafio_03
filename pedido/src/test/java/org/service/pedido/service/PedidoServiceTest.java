package org.service.pedido.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.service.pedido.dto.PedidoResponseDTO;
import org.service.pedido.feign.ClienteClient;
import org.service.pedido.feign.EstoqueClient;
import org.service.pedido.model.Pedido;
import org.service.pedido.repository.PedidoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private EstoqueClient estoqueClient;

    @Mock
    private ClienteClient clienteClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void deveRetornarTodosOsPedidos() {
        Pedido pedido1 = new Pedido(1L, "Cliente 1", Map.of(1L, 2), "CONFIRMADO");
        Pedido pedido2 = new Pedido(2L, "Cliente 2", Map.of(2L, 5), "ENVIADO");
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido1, pedido2));

        List<PedidoResponseDTO> pedidos = pedidoService.findAll();

        assertNotNull(pedidos);
        assertEquals(2, pedidos.size());
        assertEquals("Cliente 1", pedidos.get(0).getCliente());
        assertEquals("Cliente 2", pedidos.get(1).getCliente());
    }

    @Test
    void deveAtualizarStatusDoPedido() {
        Pedido pedido = new Pedido(1L, "Cliente 1", Map.of(1L, 2), "CONFIRMADO");

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        PedidoResponseDTO response = pedidoService.atualizarStatus(1L, "ENVIADO");

        assertNotNull(response);
        assertEquals("ENVIADO", response.getStatus());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void deveDeletarPedidoPorId() {
        when(pedidoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pedidoRepository).deleteById(1L);

        assertDoesNotThrow(() -> pedidoService.deletarPedido(1L));
        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveVerificarSeProdutoEstaEmPedido() {
        Pedido pedido = new Pedido(1L, "Cliente 1", Map.of(1L, 2), "CONFIRMADO");

        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        boolean isInPedido = pedidoService.isProdutoInPedido(1L);

        assertTrue(isInPedido);
        verify(pedidoRepository, times(1)).findAll();
    }
}
