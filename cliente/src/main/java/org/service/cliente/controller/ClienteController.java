package org.service.cliente.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.service.cliente.dto.ClienteResponseDTO;
import org.service.cliente.model.Cliente;
import org.service.cliente.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Endpoints para gerenciar clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Buscar todos os clientes", description = "Busca todos os clientes com seus pedidos associados")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientesComPedidos() {
        List<ClienteResponseDTO> clientesComPedidos = clienteService.findAllClientesComPedidos();
        return ResponseEntity.ok(clientesComPedidos);
    }

    @Operation(summary = "Buscar cliente por ID", description = "Busca os detalhes de um cliente específico pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        return clienteService.findClienteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Criar um novo cliente", description = "Cria um cliente e retorna os detalhes do cliente criado")
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.createCliente(cliente));
    }

    @Operation(summary = "Atualizar cliente", description = "Atualiza os detalhes de um cliente específico pelo ID")
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            Cliente updatedCliente = clienteService.updateCliente(id, cliente);
            return ResponseEntity.ok(updatedCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deletar cliente", description = "Remove um cliente específico pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        try {
            clienteService.deleteCliente(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
