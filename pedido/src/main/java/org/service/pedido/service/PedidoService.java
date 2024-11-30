package org.service.pedido.service;

import org.service.pedido.dto.PedidoRequestDTO;
import org.service.pedido.dto.PedidoResponseDTO;
import org.service.pedido.dto.ProdutoEstoqueDTO;
import org.service.pedido.feign.ClienteClient;
import org.service.pedido.feign.EstoqueClient;
import org.service.pedido.model.Pedido;
import org.service.pedido.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EstoqueClient estoqueClient;

    @Autowired
    private ClienteClient clienteClient;

    public PedidoService(PedidoRepository pedidoRepository, EstoqueClient estoqueClient) {
        this.pedidoRepository = pedidoRepository;
        this.estoqueClient = estoqueClient;
    }

    public PedidoResponseDTO criarPedido(PedidoRequestDTO pedidoRequestDTO) {
        ResponseEntity<Object> clienteResponse = clienteClient.getClienteById(pedidoRequestDTO.getCliente());
        if (clienteResponse.getBody() == null) {
            throw new IllegalArgumentException("Cliente com ID " + pedidoRequestDTO.getCliente() + " não encontrado.");
        }

        logger.info("Cliente encontrado: ID {}", pedidoRequestDTO.getCliente());

        pedidoRequestDTO.getProdutos().forEach((produtoId, quantidade) -> {
            logger.info("Atualizando estoque para Produto ID: {} com Quantidade: {}", produtoId, quantidade);
            estoqueClient.atualizarQuantidade(produtoId, quantidade);
        });

        Pedido pedido = new Pedido();
        pedido.setCliente(pedidoRequestDTO.getCliente().toString());
        pedido.setProdutos(pedidoRequestDTO.getProdutos());
        pedido.setStatus("CONFIRMADO");

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        logger.info("Pedido criado com sucesso! ID: {}, Cliente ID: {}", pedidoSalvo.getId(), pedidoSalvo.getCliente());

        return new PedidoResponseDTO(
                pedidoSalvo.getId(),
                pedidoSalvo.getCliente(),
                pedidoSalvo.getProdutos(),
                pedidoSalvo.getStatus()
        );
    }

    public List<PedidoResponseDTO> findAll() {
        logger.info("Buscando todos os pedidos no sistema...");
        List<PedidoResponseDTO> pedidos = pedidoRepository.findAll().stream()
                .map(pedido -> new PedidoResponseDTO(
                        pedido.getId(),
                        pedido.getCliente(),
                        pedido.getProdutos(),
                        pedido.getStatus()
                ))
                .collect(Collectors.toList());
        logger.info("Total de pedidos encontrados: {}", pedidos.size());
        return pedidos;
    }

    public PedidoResponseDTO buscarPedidoPorId(Long id) {
        logger.info("Buscando pedido pelo ID: {}", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pedido não encontrado com o ID: {}", id);
                    return new IllegalArgumentException("Pedido não encontrado com o ID: " + id);
                });

        logger.info("Pedido encontrado: ID: {}, Cliente: {}", pedido.getId(), pedido.getCliente());
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getCliente(),
                pedido.getProdutos(),
                pedido.getStatus()
        );
    }

    public PedidoResponseDTO atualizarStatus(Long id, String novoStatus) {
        logger.info("Atualizando status do pedido ID: {} para: {}", id, novoStatus);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pedido não encontrado para o ID: {}", id);
                    return new IllegalArgumentException("Pedido não encontrado com o ID: " + id);
                });

        pedido.setStatus(novoStatus);
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        logger.info("Status do pedido ID: {} atualizado para: {}", pedidoAtualizado.getId(), novoStatus);

        return new PedidoResponseDTO(
                pedidoAtualizado.getId(),
                pedidoAtualizado.getCliente(),
                pedidoAtualizado.getProdutos(),
                pedidoAtualizado.getStatus()
        );
    }

    public void deletarPedido(Long id) {
        logger.info("Deletando pedido ID: {}", id);
        if (!pedidoRepository.existsById(id)) {
            logger.error("Pedido com ID {} não encontrado", id);
            throw new IllegalArgumentException("Pedido com ID " + id + " não encontrado.");
        }
        pedidoRepository.deleteById(id);
        logger.info("Pedido ID: {} deletado com sucesso!", id);
    }

    public void deletarTodosOsPedidos() {
        logger.warn("Deletando todos os pedidos do sistema...");
        pedidoRepository.deleteAll();
        logger.info("Todos os pedidos foram deletados com sucesso.");
    }

    public List<PedidoResponseDTO> findByClienteId(Long clienteId) {
        logger.info("Buscando pedidos para o cliente ID: {}", clienteId);

        List<Pedido> pedidos = pedidoRepository.findByCliente(clienteId.toString());

        return pedidos.stream()
                .map(pedido -> new PedidoResponseDTO(
                        pedido.getId(),
                        pedido.getCliente(),
                        pedido.getProdutos(),
                        pedido.getStatus()
                ))
                .collect(Collectors.toList());
    }


}
