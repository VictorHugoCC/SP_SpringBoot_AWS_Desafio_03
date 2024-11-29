package org.service.pedido.service;

import org.service.pedido.dto.PedidoRequestDTO;
import org.service.pedido.dto.PedidoResponseDTO;
import org.service.pedido.dto.PedidoStatusDTO;
import org.service.pedido.model.Pedido;
import org.service.pedido.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);
    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<PedidoResponseDTO> findAll() {
        logger.info("Buscando todos os pedidos...");
        return pedidoRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO findById(Long id) {
        logger.info("Buscando pedido com ID {}", id);
        Pedido pedido = findPedidoOrThrow(id);
        return convertToResponseDTO(pedido);
    }

    public PedidoResponseDTO save(PedidoRequestDTO pedidoRequestDTO) {
        logger.info("Salvando novo pedido para o cliente: {}", pedidoRequestDTO.getCliente());
        Pedido pedido = new Pedido(
                null,
                pedidoRequestDTO.getCliente(),
                pedidoRequestDTO.getStatus(),
                pedidoRequestDTO.getTotal(),
                null
        );
        Pedido savedPedido = pedidoRepository.save(pedido);
        return convertToResponseDTO(savedPedido);
    }

    public PedidoResponseDTO atualizarStatus(Long id, PedidoStatusDTO statusDTO) {
        logger.info("Atualizando status do pedido com ID {} para {}", id, statusDTO.getStatus());
        Pedido pedido = findPedidoOrThrow(id);
        pedido.setStatus(statusDTO.getStatus());
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return convertToResponseDTO(pedidoAtualizado);
    }

    public void deleteById(Long id) {
        logger.info("Deletando pedido com ID {}", id);
        Pedido pedido = findPedidoOrThrow(id);
        pedidoRepository.delete(pedido);
    }

    private Pedido findPedidoOrThrow(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido com ID " + id + " não encontrado."));
    }

    private PedidoResponseDTO convertToResponseDTO(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getCliente(),
                pedido.getStatus(),
                pedido.getTotal(),
                pedido.getData()
        );
    }
}
