package org.service.cliente.controller;

import org.service.cliente.dto.ClienteRequestDTO;
import org.service.cliente.dto.ClienteResponseDTO;
import org.service.cliente.model.Cliente;
import org.service.cliente.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteResponseDTO> getAllClientes() {
        return clienteService.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getClienteById(@PathVariable Long id) {
        return clienteService.findById(id)
                .map(cliente -> ResponseEntity.ok(convertToResponseDTO(cliente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ClienteResponseDTO createCliente(@RequestBody ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = convertToEntity(clienteRequestDTO);
        return convertToResponseDTO(clienteService.save(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> updateCliente(@PathVariable Long id, @RequestBody ClienteRequestDTO clienteRequestDTO) {
        return clienteService.findById(id)
                .map(existingCliente -> {
                    Cliente cliente = convertToEntity(clienteRequestDTO);
                    cliente.setId(existingCliente.getId());
                    return ResponseEntity.ok(convertToResponseDTO(clienteService.save(cliente)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        if (clienteService.findById(id).isPresent()) {
            clienteService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ClienteResponseDTO convertToResponseDTO(Cliente cliente) {
        ClienteResponseDTO responseDTO = new ClienteResponseDTO();
        responseDTO.setId(cliente.getId());
        responseDTO.setNome(cliente.getNome());
        responseDTO.setEmail(cliente.getEmail());
        responseDTO.setTelefone(cliente.getTelefone());
        return responseDTO;
    }

    private Cliente convertToEntity(ClienteRequestDTO requestDTO) {
        Cliente cliente = new Cliente();
        cliente.setNome(requestDTO.getNome());
        cliente.setEmail(requestDTO.getEmail());
        cliente.setTelefone(requestDTO.getTelefone());
        return cliente;
    }
}
