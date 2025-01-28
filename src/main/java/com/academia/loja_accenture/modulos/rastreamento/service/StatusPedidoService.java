package com.academia.loja_accenture.modulos.rastreamento.service;

import com.academia.loja_accenture.core.exceptions.PedidoNotFoundException;
import com.academia.loja_accenture.core.exceptions.ResourceNotFound;
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j // Habilita o uso de logs para registrar eventos do serviço.
@Service
@RequiredArgsConstructor
public class StatusPedidoService {

    private final PedidoRepository pedidoRepository;
    private final StatusPedidoRepository statusPedidoRepository;

//    @Transactional
    public RegistrarStatusResponseDTO registrarStatus(Long pedidoId, RegistrarStatusRequestDTO request) {
        log.info("Iniciando registro de status para o pedido com ID: {}", pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.error("Pedido não encontrado com o ID: {}", pedidoId);
                    return new PedidoNotFoundException("Pedido não encontrado com o ID " + pedidoId);
                });

        log.info("Pedido encontrado com ID: {}", pedidoId);

        StatusPedido statusPedido = new StatusPedido();
        statusPedido.getPedidos().add(pedido);
        statusPedido.setDescricao(request.getDescricao());

        StatusPedido createdStatus = statusPedidoRepository.save(statusPedido);

        log.info("Status registrado com sucesso para o pedido ID {}. Status ID: {}, Descrição: {}",
                pedidoId, createdStatus.getId(), createdStatus.getDescricao());

        return new RegistrarStatusResponseDTO(
                createdStatus.getId(),
                createdStatus.getDescricao(),
                createdStatus.getCreatedAt()
        );
    }

    public List<RegistrarStatusResponseDTO> obterHistorico(Long pedidoId) {
        log.info("Obtendo histórico de status para o pedido com ID: {}", pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.error("Pedido não encontrado com o ID: {}", pedidoId);
                    return new PedidoNotFoundException("Pedido não encontrado com o ID " + pedidoId);
                });
      
        log.info("Histórico de status encontrado para o pedido ID: {}", pedidoId);

        return pedido.getHistoricoStatus().stream()
                .map(status -> new RegistrarStatusResponseDTO(
                        status.getId(),
                        status.getDescricao(),
                        status.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public RegistrarStatusResponseDTO obterStatusAtual(Long pedidoId) {
        log.info("Obtendo status atual para o pedido com ID: {}", pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.error("Pedido não encontrado com o ID: {}", pedidoId);
                    return new PedidoNotFoundException("Pedido não encontrado com o ID " + pedidoId);
                });
        
        return pedido.getHistoricoStatus().stream()
                .max(Comparator.comparing(historico -> historico.getCreatedAt()))
                .map(status -> {
                    log.info("Status atual encontrado para o pedido ID {}: Status ID {}, Descrição: {}",
                            pedidoId, status.getId(), status.getDescricao());
                    return new RegistrarStatusResponseDTO(
                            status.getId(),
                            status.getDescricao(),
                            status.getCreatedAt()
                    );
                })
                .orElseThrow(() -> {
                    log.error("Nenhum status encontrado para o pedido com ID: {}", pedidoId);
                    return new ResourceNotFound("Nenhum status encontrado para o pedido com ID " + pedidoId);
                });
    }
}