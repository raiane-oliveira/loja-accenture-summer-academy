package com.academia.loja_accenture.modulos.usuario.service;

import com.academia.loja_accenture.core.exceptions.ClienteNotFoundException;
import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.dto.AtualizarClienteDTO;
import com.academia.loja_accenture.modulos.usuario.dto.ClienteDTO;
import com.academia.loja_accenture.modulos.usuario.dto.RegistrarClienteDTO;
import com.academia.loja_accenture.modulos.usuario.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteDTO save(RegistrarClienteDTO data) {
        Cliente cliente = new Cliente();
        cliente.setNome(data.nome());
        cliente.setEmail(data.email());
        cliente.setSenha(passwordEncoder.encode(data.senha()));

        return convertToDTO(clienteRepository.save(cliente));
    }

    public void update(Long clienteId, AtualizarClienteDTO data) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(ClienteNotFoundException::new);

        if (data.nome() != null) {
            cliente.setNome(data.nome());
        }
        if (data.email() != null) {
            cliente.setEmail(data.email());
        }
        if (data.senha() != null) {
            cliente.setSenha(passwordEncoder.encode(data.senha()));
        }

        clienteRepository.save(cliente);
    }

    public ClienteDTO getById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(ClienteNotFoundException::new);

        return convertToDTO(cliente);
    }

    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ClienteDTO convertToDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail()
        );
    }
}