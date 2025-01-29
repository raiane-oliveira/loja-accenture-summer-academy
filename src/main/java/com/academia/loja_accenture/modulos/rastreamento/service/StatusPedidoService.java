package com.academia.loja_accenture.modulos.rastreamento.service;

import com.academia.loja_accenture.core.exceptions.PedidoNotFoundException;
import com.academia.loja_accenture.core.exceptions.ResourceNotFoundException;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import com.academia.loja_accenture.modulos.rastreamento.domain.StatusPedido;
import com.academia.loja_accenture.modulos.rastreamento.dto.RegistrarStatusRequestDTO;
import com.academia.loja_accenture.modulos.rastreamento.dto.RegistrarStatusResponseDTO;
import com.academia.loja_accenture.modulos.rastreamento.repository.StatusPedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j // Habilita logs para monitoramento do serviço
@Service
@RequiredArgsConstructor
public class StatusPedidoService {

    private final PedidoRepository pedidoRepository;
    private final StatusPedidoRepository statusPedidoRepository;

    @Transactional
    public RegistrarStatusResponseDTO registrarStatus(Long pedidoId, RegistrarStatusRequestDTO request) {
        log.info("Iniciando registro de status para o pedido com ID: {}", pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.error("Pedido não encontrado com o ID: {}", pedidoId);
                    return new PedidoNotFoundException("Pedido não encontrado com o ID " + pedidoId);
                });

        log.info("Pedido encontrado com ID: {}", pedidoId);

        StatusPedido statusPedido = new StatusPedido();
        statusPedido.setPedido(pedido);
        statusPedido.setStatus(request.getStatus());

        StatusPedido createdStatus = statusPedidoRepository.save(statusPedido);

        log.info("Status registrado com sucesso para o pedido ID {}. Status ID: {}, Status: {}",
                pedidoId, createdStatus.getId(), createdStatus.getStatus());

        return new RegistrarStatusResponseDTO(
                createdStatus.getId(),
                createdStatus.getStatus(),
                createdStatus.getCreatedAt()
        );
    }

    public List<RegistrarStatusResponseDTO> obterHistorico(Long pedidoId) {
        log.info("Obtendo histórico de status para o pedido com ID: {}", pedidoId);

        if (!pedidoRepository.existsById(pedidoId)) {
            log.error("Pedido não encontrado com o ID: {}", pedidoId);
            throw new PedidoNotFoundException("Pedido não encontrado com o ID " + pedidoId);
        }

        List<StatusPedido> historico = statusPedidoRepository.findByPedidoIdOrderByCreatedAtDesc(pedidoId);

        if (historico.isEmpty()) {
            log.warn("Nenhum histórico de status encontrado para o pedido ID: {}", pedidoId);
            throw new ResourceNotFoundException("Nenhum status encontrado para o pedido com ID " + pedidoId);
        }

        log.info("Histórico de status encontrado para o pedido ID: {}", pedidoId);

        return historico.stream()
                .map(status -> new RegistrarStatusResponseDTO(
                        status.getId(),
                        status.getStatus(),
                        status.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public RegistrarStatusResponseDTO obterStatusAtual(Long pedidoId) {
        log.info("Obtendo status atual para o pedido com ID: {}", pedidoId);

        if (!pedidoRepository.existsById(pedidoId)) {
            log.error("Pedido não encontrado com o ID: {}", pedidoId);
            throw new PedidoNotFoundException("Pedido não encontrado com o ID " + pedidoId);
        }

        StatusPedido statusAtual = statusPedidoRepository.findTopByPedidoIdOrderByCreatedAtDesc(pedidoId)
                .orElseThrow(() -> {
                    log.error("Nenhum status encontrado para o pedido com ID: {}", pedidoId);
                    return new ResourceNotFoundException("Nenhum status encontrado para o pedido com ID " + pedidoId);
                });

        log.info("Status atual encontrado para o pedido ID {}: Status ID {}, Status: {}",
                pedidoId, statusAtual.getId(), statusAtual.getStatus());

        return new RegistrarStatusResponseDTO(
                statusAtual.getId(),
                statusAtual.getStatus(),
                statusAtual.getCreatedAt()
        );
    }
}
