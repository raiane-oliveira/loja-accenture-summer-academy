package com.academia.loja_accenture.modulos.pedido;

import com.academia.loja_accenture.factory.MakeCliente;
import com.academia.loja_accenture.factory.MakeVendedor;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarPedidoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.PedidoDTO;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import com.academia.loja_accenture.modulos.pedido.service.PedidoService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VendedorRepository vendedorRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSavePedidoSuccessfully() {
        Long clienteId = 1L;
        Long vendedorId = 2L;
        List<Long> produtosIds = List.of(3L, 4L);
        CadastrarPedidoDTO data = new CadastrarPedidoDTO(
            clienteId,
            vendedorId,
            produtosIds,
            "Pedido de teste"
        );
        
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        
        Vendedor vendedor = new Vendedor();
        vendedor.setId(vendedorId);
        
        Produto produto1 = new Produto();
        produto1.setId(3L);
        produto1.setValor(BigDecimal.valueOf(50.00));
        produto1.setVendedor(vendedor);
        
        Produto produto2 = new Produto();
        produto2.setId(4L);
        produto2.setValor(BigDecimal.valueOf(30.00));
        produto2.setVendedor(vendedor);
        
        Pedido pedido = new Pedido();
        pedido.setId(10L);
        pedido.setDescricao(data.descricao());
        pedido.setCliente(cliente);
        pedido.setVendedor(vendedor);
        pedido.setProdutos(List.of(produto1, produto2));
        pedido.setValor(BigDecimal.valueOf(80.00));
        pedido.setQuantidade(2);
        
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(vendedorRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findAllById(produtosIds)).thenReturn(List.of(produto1, produto2));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        
        PedidoDTO result = pedidoService.save(data);
        
        assertNotNull(result);
        assertEquals(10L, result.id());
        assertEquals("Pedido de teste", result.descricao());
        assertEquals(BigDecimal.valueOf(80.00), result.valor());
        assertEquals(2, result.quantidade());
        verify(clienteRepository, times(1)).findById(clienteId);
        verify(vendedorRepository, times(1)).findById(vendedorId);
        verify(produtoRepository, times(1)).findAllById(produtosIds);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void shouldThrowExceptionWhenClienteNotFound() {
        Long clienteId = 1L;
        Long vendedorId = 2L;
        List<Long> produtosIds = List.of(3L, 4L);
        CadastrarPedidoDTO data = new CadastrarPedidoDTO(
            clienteId,
            vendedorId,
            produtosIds,
            "Pedido de teste"
        );
        
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> pedidoService.save(data)
        );
        
        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenVendedorNotFound() {
        Long clienteId = 1L;
        Long vendedorId = 2L;
        List<Long> produtosIds = List.of(3L, 4L);
        CadastrarPedidoDTO data = new CadastrarPedidoDTO(
            clienteId,
            vendedorId,
            produtosIds,
            "Pedido de teste"
        );
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(vendedorRepository.findById(vendedorId)).thenReturn(Optional.empty());
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> pedidoService.save(data)
        );

        assertEquals("Vendedor não encontrado", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNoProductsFound() {
        Long clienteId = 1L;
        Long vendedorId = 2L;
        List<Long> produtosIds = List.of(3L, 4L);
        CadastrarPedidoDTO data = new CadastrarPedidoDTO(
            clienteId,
            vendedorId,
            produtosIds,
            "Pedido de teste"
        );
        
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        
        Vendedor vendedor = new Vendedor();
        vendedor.setId(vendedorId);
        
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(vendedorRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findAllById(produtosIds)).thenReturn(List.of());
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> pedidoService.save(data)
        );
        
        assertEquals("Nenhum produto encontrado", exception.getMessage());
    }

    @Test
    void shouldGetPedidoById() {
        Cliente cliente = MakeCliente.create();
        Vendedor vendedor = MakeVendedor.create();
        
        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Produto Teste 2");
        produto1.setDescricao("Descrição Teste");
        produto1.setValor(BigDecimal.valueOf(10.0));
        produto1.setVendedor(vendedor);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto1));
        
        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Produto Teste 2");
        produto2.setDescricao("Descrição Teste 2");
        produto2.setValor(BigDecimal.valueOf(11.0));
        produto2.setVendedor(vendedor);
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(produto2));

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setDescricao("Pedido de teste");
        pedido.setCliente(cliente);
        pedido.setVendedor(vendedor);
        pedido.setProdutos(List.of(produto1, produto2));
        pedido.setValor(produto1.getValor().add(produto2.getValor()));
        pedido.setQuantidade(2);
        
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        PedidoDTO fetchedPedido = pedidoService.getById(1L);

        assertNotNull(fetchedPedido);
        assertEquals(pedido.getId(), fetchedPedido.id());
        assertEquals(pedido.getDescricao(), fetchedPedido.descricao());
        assertEquals(pedido.getValor(), fetchedPedido.valor());
        assertEquals(2, fetchedPedido.produtos().size());
    }

    @Test
    void shouldThrowExceptionWhenPedidoNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> pedidoService.getById(999L)
        );

        assertEquals("Pedido não encontrado", exception.getMessage());
    }
}