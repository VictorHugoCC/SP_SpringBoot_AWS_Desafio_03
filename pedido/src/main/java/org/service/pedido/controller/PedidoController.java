package org.service.pedido.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.service.pedido.dto.PedidoRequestDTO;
import org.service.pedido.dto.PedidoResponseDTO;
import org.service.pedido.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para gerenciar pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido e verifica o estoque disponível")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido criado com sucesso",
                    content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody PedidoRequestDTO pedidoRequestDTO) {
        PedidoResponseDTO pedidoResponseDTO = pedidoService.criarPedido(pedidoRequestDTO);
        return ResponseEntity.ok(pedidoResponseDTO);
    }

    @GetMapping
    @Operation(summary = "Buscar todos os pedidos", description = "Retorna a lista de todos os pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> buscarTodosPedidos() {
        return ResponseEntity.ok(pedidoService.findAll());
    }

    @GetMapping("/produto")
    public ResponseEntity<Boolean> isProdutoInPedido(@RequestParam Long produtoId) {
        boolean isInPedido = pedidoService.isProdutoInPedido(produtoId);
        return ResponseEntity.ok(isInPedido);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Busca os detalhes de um pedido específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado",
                    content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    public ResponseEntity<PedidoResponseDTO> buscarPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPedidoPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido existente")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        PedidoResponseDTO pedidoAtualizado = pedidoService.atualizarStatus(id, status);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pedido", description = "Deleta um pedido específico pelo ID")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        pedidoService.deletarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Deletar todos os pedidos", description = "Remove todos os pedidos do sistema")
    public ResponseEntity<Void> deletarTodosOsPedidos() {
        pedidoService.deletarTodosOsPedidos();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar pedidos por cliente", description = "Retorna a lista de pedidos associados a um cliente")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidosByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.findByClienteId(clienteId));
    }
}
