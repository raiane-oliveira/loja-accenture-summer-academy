package com.academia.loja_accenture.modulos.rastreamento.service;

import com.academia.loja_accenture.modulos.rastreamento.domain.StatusPedido;
import com.academia.loja_accenture.modulos.rastreamento.repository.StatusPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusPedidoService {
    private final StatusPedidoRepository statusPedidoRepository;

    public StatusPedido salvar(StatusPedido statusPedido) {
        return statusPedidoRepository.save(statusPedido);
    }

    public List<StatusPedido> listarTodos() {
        return statusPedidoRepository.findAll();
    }

    public StatusPedido buscarPorId(Long id) {
        return statusPedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Status não encontrado."));
    }
}
