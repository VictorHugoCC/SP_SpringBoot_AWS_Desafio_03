package org.service.pedido.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.service.pedido.dto.PedidoRequestDTO;
import org.service.pedido.dto.PedidoResponseDTO;
import org.service.pedido.dto.ProdutoEstoqueDTO;
import org.service.pedido.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCriarPedidoComSucesso() throws Exception {
        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(1L, Map.of(1L, 2));
        PedidoResponseDTO pedidoResponseDTO = new PedidoResponseDTO(1L, "Cliente 1", Map.of(1L, 2), "CONFIRMADO");

        Mockito.when(pedidoService.criarPedido(any(PedidoRequestDTO.class))).thenReturn(pedidoResponseDTO);

        String requestBody = objectMapper.writeValueAsString(pedidoRequestDTO);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cliente").value("Cliente 1"))
                .andExpect(jsonPath("$.produtos['1']").value(2)) // Ajustado para acessar chaves do mapa
                .andExpect(jsonPath("$.status").value("CONFIRMADO"));
    }

    @Test
    void deveRetornarTodosOsPedidos() throws Exception {
        PedidoResponseDTO pedido1 = new PedidoResponseDTO(1L, "Cliente 1", Map.of(1L, 2), "CONFIRMADO");
        PedidoResponseDTO pedido2 = new PedidoResponseDTO(2L, "Cliente 2", Map.of(2L, 3), "ENVIADO");

        Mockito.when(pedidoService.findAll()).thenReturn(Arrays.asList(pedido1, pedido2));

        mockMvc.perform(get("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].cliente").value("Cliente 1"))
                .andExpect(jsonPath("$[0].produtos['1']").value(2)) // Ajustado
                .andExpect(jsonPath("$[0].status").value("CONFIRMADO"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].cliente").value("Cliente 2"))
                .andExpect(jsonPath("$[1].produtos['2']").value(3)) // Ajustado
                .andExpect(jsonPath("$[1].status").value("ENVIADO"));
    }

    @Test
    void deveRetornarPedidoPorId() throws Exception {
        PedidoResponseDTO pedidoResponseDTO = new PedidoResponseDTO(1L, "Cliente 1", Map.of(1L, 2), "CONFIRMADO");

        Mockito.when(pedidoService.buscarPedidoPorId(1L)).thenReturn(pedidoResponseDTO);

        mockMvc.perform(get("/pedidos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cliente").value("Cliente 1"))
                .andExpect(jsonPath("$.produtos['1']").value(2)) // Ajustado
                .andExpect(jsonPath("$.status").value("CONFIRMADO"));
    }

    @Test
    void deveAtualizarStatusDoPedido() throws Exception {
        PedidoResponseDTO pedidoAtualizado = new PedidoResponseDTO(1L, "Cliente 1", Map.of(1L, 2), "ENVIADO");

        Mockito.when(pedidoService.atualizarStatus(eq(1L), eq("ENVIADO"))).thenReturn(pedidoAtualizado);

        mockMvc.perform(put("/pedidos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "status": "ENVIADO"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cliente").value("Cliente 1"))
                .andExpect(jsonPath("$.produtos['1']").value(2)) // Ajustado
                .andExpect(jsonPath("$.status").value("ENVIADO"));
    }

    @Test
    void deveRetornarPedidosPorClienteId() throws Exception {
        PedidoResponseDTO pedido1 = new PedidoResponseDTO(1L, "Cliente 1", Map.of(1L, 2), "CONFIRMADO");
        PedidoResponseDTO pedido2 = new PedidoResponseDTO(2L, "Cliente 1", Map.of(2L, 3), "ENVIADO");

        Mockito.when(pedidoService.findByClienteId(1L)).thenReturn(List.of(pedido1, pedido2));

        mockMvc.perform(get("/pedidos/cliente/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].cliente").value("Cliente 1"))
                .andExpect(jsonPath("$[0].produtos['1']").value(2)) // Ajustado
                .andExpect(jsonPath("$[0].status").value("CONFIRMADO"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].cliente").value("Cliente 1"))
                .andExpect(jsonPath("$[1].produtos['2']").value(3)) // Ajustado
                .andExpect(jsonPath("$[1].status").value("ENVIADO"));
    }

}
