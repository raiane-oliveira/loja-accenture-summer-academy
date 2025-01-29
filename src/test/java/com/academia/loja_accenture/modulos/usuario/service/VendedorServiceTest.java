package com.academia.loja_accenture.modulos.usuario.service;

import com.academia.loja_accenture.core.exceptions.VendedorNotFoundException;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.dto.*;
import com.academia.loja_accenture.modulos.usuario.dto.RegistrarVendedorDTO;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendedorServiceTest {

    @InjectMocks
    private VendedorService vendedorService;

    @Mock
    private VendedorRepository vendedorRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    private AtualizarVendedorDTO atualizarVendedorDTO;
    private Vendedor vendedor;
    
    @BeforeEach
    void setUp() {
        atualizarVendedorDTO = new AtualizarVendedorDTO("Vendedor Atualizado", "Setor atualizado");
        
        vendedor = new Vendedor();
        vendedor.setId(1L);
        vendedor.setNome("Vendedor Teste");
        vendedor.setEmail("teste@example.com");
        vendedor.setSenha("senhaCriptografada");
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

        assertThrows(VendedorNotFoundException.class, () -> vendedorService.getById(1L));
        verify(vendedorRepository, times(1)).findById(1L);
    }
    
    @Test
    void update_validRequest_shouldUpdateVendedor() {
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        
        vendedorService.update(1L, atualizarVendedorDTO);
        
        assertThat(vendedor.getNome()).isEqualTo("Vendedor Atualizado");
        assertThat(vendedor.getSetor()).isEqualTo("Setor atualizado");
        
        verify(vendedorRepository, times(1)).findById(1L);
        verify(vendedorRepository, times(1)).save(vendedor);
    }
    
    @Test
    void update_vendedorNotFound_shouldThrowVendedorNotFoundException() {
        when(vendedorRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(VendedorNotFoundException.class, () -> vendedorService.update(1L, atualizarVendedorDTO));
        verify(vendedorRepository, times(1)).findById(1L);
        verify(vendedorRepository, never()).save(any(Vendedor.class));
    }
    
    @Test
    void listarTodos_shouldReturnListOfVendedorDTO() {
        when(vendedorRepository.findAll()).thenReturn(List.of(vendedor));
        
        List<VendedorDTO> response = vendedorService.listarTodos();
        
        assertThat(response).hasSize(1);
        assertThat(response.getFirst().id()).isEqualTo(1L);
        assertThat(response.getFirst().nome()).isEqualTo("Vendedor Teste");
        assertThat(response.getFirst().email()).isEqualTo("teste@example.com");
        
        verify(vendedorRepository, times(1)).findAll();
    }
    
    @Test
    void listarTodos_semVendedores_shouldReturnEmptyList() {
        when(vendedorRepository.findAll()).thenReturn(List.of());
        
        List<VendedorDTO> response = vendedorService.listarTodos();
        
        assertThat(response).isEmpty();
        verify(vendedorRepository, times(1)).findAll();
    }
}
