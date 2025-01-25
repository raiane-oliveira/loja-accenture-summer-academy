package com.academia.loja_accenture.modulos.usuario.service;

import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.dto.RegistrarVendedorDTO;
import com.academia.loja_accenture.modulos.usuario.dto.VendedorDTO;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VendedorServiceTest {

    @InjectMocks
    private VendedorService vendedorService;

    @Mock
    private VendedorRepository vendedorRepository;

    public VendedorServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarVendedorComSucesso() {
        RegistrarVendedorDTO dto = new RegistrarVendedorDTO("Maria", "Tech", "maria@gmail.com", "senha123");
        Vendedor vendedor = new Vendedor();
        vendedor.setNome("Maria");
        vendedor.setSetor("Tech");
        vendedor.setEmail("maria@gmail.com");
        vendedor.setSenha("senha123");

        Vendedor vendedorSalvo = new Vendedor();
        vendedorSalvo.setId(1L);
        vendedorSalvo.setNome("Maria");
        vendedorSalvo.setSetor("Tech");
        vendedorSalvo.setEmail("maria@gmail.com");
        vendedorSalvo.setSenha("senha123");

        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(vendedorSalvo);

        VendedorDTO resultado = vendedorService.save(dto);

        assertNotNull(resultado);
        assertEquals("Maria", resultado.nome());
        assertEquals("Tech", resultado.setor());
        assertEquals("maria@gmail.com", resultado.email());
        verify(vendedorRepository, times(1)).save(any(Vendedor.class));
    }

    @Test
    void deveRetornarVendedorPorId() {
        Vendedor vendedor = new Vendedor();
        vendedor.setId(1L);
        vendedor.setNome("Carlos");
        vendedor.setSetor("Vendas");
        vendedor.setEmail("carlos@empresa.com");
        vendedor.setSenha("senha789");

        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));

        VendedorDTO resultado = vendedorService.getById(1L);

        assertNotNull(resultado);
        assertEquals("Carlos", resultado.nome());
        assertEquals("Vendas", resultado.setor());
        assertEquals("carlos@empresa.com", resultado.email());
        verify(vendedorRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoVendedorNaoExistir() {
        when(vendedorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> vendedorService.getById(1L));
        verify(vendedorRepository, times(1)).findById(1L);
    }
}
