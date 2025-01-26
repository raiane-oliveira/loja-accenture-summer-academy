package com.academia.loja_accenture.teste.unitario;

import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarPedidoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.PedidoDTO;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.repository.ClienteRepository;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService; // Classe que estamos testando

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VendedorRepository vendedorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
    }

    @Test
    void deveSalvarPedidoComSucesso() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Vendedor vendedor = new Vendedor();
        vendedor.setId(1L);

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setValor(BigDecimal.valueOf(50.00));

        CadastrarPedidoDTO data = new CadastrarPedidoDTO(
                "Pedido Teste",
                1L, // Cliente ID
                1L, // Vendedor ID
                List.of(1L) // Lista de IDs de produtos
        );

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findAllById(data.produtosIds())).thenReturn(List.of(produto));

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setDescricao("Pedido Teste");
        pedido.setValor(BigDecimal.valueOf(50.00));
        pedido.setCliente(cliente);
        pedido.setVendedor(vendedor);
        pedido.setProdutos(List.of(produto));
        pedido.setQuantidade(1);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act
        PedidoDTO pedidoDTO = pedidoService.save(data);

        // Assert
        assertNotNull(pedidoDTO);
        assertEquals(1L, pedidoDTO.id());
        assertEquals("Pedido Teste", pedidoDTO.descricao());
        assertEquals(BigDecimal.valueOf(50.00), pedidoDTO.valor());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoForEncontrado() {
        // Arrange
        CadastrarPedidoDTO data = new CadastrarPedidoDTO(
                "Pedido Teste",
                1L, // Cliente ID
                1L, // Vendedor ID
                List.of(1L) // Lista de IDs de produtos
        );

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> pedidoService.save(data));
        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNenhumProdutoForEncontrado() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Vendedor vendedor = new Vendedor();
        vendedor.setId(1L);

        CadastrarPedidoDTO data = new CadastrarPedidoDTO(
                "Pedido Teste",
                1L, // Cliente ID
                1L, // Vendedor ID
                List.of(1L) // Lista de IDs de produtos
        );

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findAllById(data.produtosIds())).thenReturn(List.of());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> pedidoService.save(data));
        assertEquals("Nenhum produto encontrado", exception.getMessage());
    }
}

