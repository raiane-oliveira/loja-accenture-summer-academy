package com.academia.loja_accenture.modulos.usuario.service;

import com.academia.loja_accenture.core.exceptions.InvalidCredentialsException;
import com.academia.loja_accenture.core.exceptions.VendedorNotFoundException;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.dto.AtualizarVendedorDTO;
import com.academia.loja_accenture.modulos.usuario.dto.RegistrarVendedorDTO;
import com.academia.loja_accenture.modulos.usuario.dto.VendedorDTO;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendedorService {
    private final VendedorRepository vendedorRepository;
    private final PasswordEncoder passwordEncoder;

    public VendedorDTO save(RegistrarVendedorDTO data) {
        if (vendedorRepository.findByEmail(data.email()).isPresent()) {
            throw new InvalidCredentialsException();
        }
        
        Vendedor vendedor = new Vendedor();
        vendedor.setNome(data.nome());
        vendedor.setSetor(data.setor());
        vendedor.setEmail(data.email());
        vendedor.setSenha(passwordEncoder.encode(data.senha()));

        return convertToDTO(vendedorRepository.save(vendedor));
    }

    public void update(Long vendedorId, AtualizarVendedorDTO data) {
        Vendedor vendedor = vendedorRepository.findById(vendedorId)
                .orElseThrow(VendedorNotFoundException::new);

        if (data.nome() != null) {
            vendedor.setNome(data.nome());
        }
        if (data.setor() != null) {
            vendedor.setSetor(data.setor());
        }

        vendedorRepository.save(vendedor);
    }

    public VendedorDTO getById(Long id) {
        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(VendedorNotFoundException::new);

        return convertToDTO(vendedor);
    }

    public List<VendedorDTO> listarTodos() {
        return vendedorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private VendedorDTO convertToDTO(Vendedor vendedor) {
        return new VendedorDTO(
                vendedor.getId(),
                vendedor.getNome(),
                vendedor.getSetor(),
                vendedor.getEmail()
        );
    }
}
