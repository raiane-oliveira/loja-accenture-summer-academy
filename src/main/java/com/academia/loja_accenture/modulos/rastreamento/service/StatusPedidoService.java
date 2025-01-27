package com.academia.loja_accenture.modulos.rastreamento.service;

import com.academia.loja_accenture.core.exceptions.PedidoNotFoundException;
import com.academia.loja_accenture.core.exceptions.ResourceNotFound;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import com.academia.loja_accenture.modulos.rastreamento.domain.PedidoStatusHistorico;
import com.academia.loja_accenture.modulos.rastreamento.dto.RegistrarStatusRequestDTO;
import com.academia.loja_accenture.modulos.rastreamento.dto.RegistrarStatusResponseDTO;
import com.academia.loja_accenture.modulos.rastreamento.repository.PedidoStatusHistoricoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j // Habilita o uso de logs para registrar eventos do serviço.
@Service
@RequiredArgsConstructor
public class StatusPedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoStatusHistoricoRepository pedidoStatusHistoricoRepository;

    public RegistrarStatusResponseDTO registrarStatus(Long pedidoId, RegistrarStatusRequestDTO request) {
        log.info("Iniciando registro de status para o pedido com ID: {}", pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.error("Pedido não encontrado com o ID: {}", pedidoId);
                    return new PedidoNotFoundException("Pedido não encontrado com o ID " + pedidoId);
                });

        log.info("Pedido encontrado com ID: {}", pedidoId);

        PedidoStatusHistorico historico = new PedidoStatusHistorico();
        historico.setPedido(pedido);
        historico.setDescricao(request.getDescricao());
        historico.setCreatedAt(LocalDateTime.now());

        PedidoStatusHistorico savedHistorico = pedidoStatusHistoricoRepository.save(historico);

        log.info("Status registrado com sucesso para o pedido ID {}. Status ID: {}, Descrição: {}",
                pedidoId, savedHistorico.getId(), savedHistorico.getDescricao());

        return new RegistrarStatusResponseDTO(
                savedHistorico.getId(),
                savedHistorico.getDescricao(),
                savedHistorico.getCreatedAt()
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