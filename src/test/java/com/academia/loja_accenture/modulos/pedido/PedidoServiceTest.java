package com.academia.loja_accenture.modulos.pedido;

import com.academia.loja_accenture.config.RabbitMQMockConfig;
import com.academia.loja_accenture.core.exceptions.*;
import com.academia.loja_accenture.factory.MakeCliente;
import com.academia.loja_accenture.factory.MakeVendedor;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarPedidoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.PedidoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.ProdutoComQuantidadeDTO;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import com.academia.loja_accenture.modulos.pedido.service.PedidoService;
import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.repository.ClienteRepository;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(RabbitMQMockConfig.class)
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
    
    @Mock
    private ObjectMapper objectMapper;
    
    @Mock
    private AmqpTemplate amqpTemplate;
    
    @Test
    void shouldSavePedidoSuccessfully() {
        Long clienteId = 1L;
        Long vendedorId = 2L;
        List<ProdutoComQuantidadeDTO> produtosComQuant = List.of(
            new ProdutoComQuantidadeDTO(3L, 2), // Produto 3 com quantidade 2
            new ProdutoComQuantidadeDTO(4L, 3)  // Produto 4 com quantidade 3
        );
        List<Long> produtosIds = produtosComQuant.stream().map(ProdutoComQuantidadeDTO::id).toList();
        
        CadastrarPedidoDTO data = new CadastrarPedidoDTO(
            clienteId,
            vendedorId,
            produtosComQuant,
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
        pedido.addProduto(produto1, 2);
        pedido.addProduto(produto2, 3);
        pedido.setValor(BigDecimal.valueOf(190.00)); // 50*2 + 30*3
        pedido.setQuantidade(5);
        
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(vendedorRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findAllById(produtosIds)).thenReturn(List.of(produto1, produto2));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        
        PedidoDTO result = pedidoService.save(data);
        
        assertNotNull(result);
        assertEquals(10L, result.id());
        assertEquals("Pedido de teste", result.descricao());
        assertEquals(BigDecimal.valueOf(190.00), result.valor()); // Verificando o valor calculado
        assertEquals(5, result.quantidade());
        verify(clienteRepository, times(1)).findById(clienteId);
        verify(vendedorRepository, times(1)).findById(vendedorId);
        verify(produtoRepository, times(1)).findAllById(produtosIds);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void shouldThrowExceptionWhenClienteNotFound() {
        Long clienteId = 1L;
        Long vendedorId = 2L;
        List<ProdutoComQuantidadeDTO> produtosComQuant = List.of(
            new ProdutoComQuantidadeDTO(3L, 1),
            new ProdutoComQuantidadeDTO(4L, 1)
        );
        CadastrarPedidoDTO data = new CadastrarPedidoDTO(
                clienteId,
                vendedorId,
                produtosComQuant,
                "Pedido de teste"
        );

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        ClienteNotFoundException exception = assertThrows(
                ClienteNotFoundException.class,
                () -> pedidoService.save(data)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenVendedorNotFound() {
        Long clienteId = 1L;
        Long vendedorId = 2L;
        List<ProdutoComQuantidadeDTO> produtosComQuant = List.of(
            new ProdutoComQuantidadeDTO(3L, 1),
            new ProdutoComQuantidadeDTO(4L, 1)
        );
        CadastrarPedidoDTO data = new CadastrarPedidoDTO(
                clienteId,
                vendedorId,
                produtosComQuant,
                "Pedido de teste"
        );
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(vendedorRepository.findById(vendedorId)).thenReturn(Optional.empty());

        VendedorNotFoundException exception = assertThrows(
                VendedorNotFoundException.class,
                () -> pedidoService.save(data)
        );

        assertEquals("Vendedor não encontrado", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNoProductsFound() {
        Long clienteId = 1L;
        Long vendedorId = 2L;
        List<ProdutoComQuantidadeDTO> produtosComQuant = List.of(
            new ProdutoComQuantidadeDTO(3L, 1),
            new ProdutoComQuantidadeDTO(4L, 1)
        );
        List<Long> produtosIds = produtosComQuant.stream().map(ProdutoComQuantidadeDTO::id).toList();
        CadastrarPedidoDTO data = new CadastrarPedidoDTO(
                clienteId,
                vendedorId,
                produtosComQuant,
                "Pedido de teste"
        );

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        Vendedor vendedor = new Vendedor();
        vendedor.setId(vendedorId);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(vendedorRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findAllById(produtosIds)).thenReturn(List.of());

         assertThrows(ProdutoNotFoundException.class, () -> pedidoService.save(data));
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
        lenient().when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto1));

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Produto Teste 2");
        produto2.setDescricao("Descrição Teste 2");
        produto2.setValor(BigDecimal.valueOf(11.0));
        produto2.setVendedor(vendedor);
        lenient().when(produtoRepository.findById(2L)).thenReturn(Optional.of(produto2));

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setDescricao("Pedido de teste");
        pedido.setCliente(cliente);
        pedido.setVendedor(vendedor);
        pedido.addProduto(produto1, 1);
        pedido.addProduto(produto2, 1);
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
        PedidoNotFoundException exception = assertThrows(PedidoNotFoundException.class,
                () -> pedidoService.getById(999L)
        );

        assertEquals("Pedido não encontrado", exception.getMessage());
    }
    
    @Test
    void shouldThrowExceptionWhenJsonProcessingFails() throws JsonProcessingException {
        Long clienteId = 1L;
        Long vendedorId = 2L;
        List<ProdutoComQuantidadeDTO> produtosComQuant = List.of(
            new ProdutoComQuantidadeDTO(3L, 1),
            new ProdutoComQuantidadeDTO(4L, 1)
        );
        List<Long> produtosIds = produtosComQuant.stream().map(ProdutoComQuantidadeDTO::id).toList();
        
        CadastrarPedidoDTO data = new CadastrarPedidoDTO(
            clienteId,
            vendedorId,
            produtosComQuant,
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
        pedido.addProduto(produto1, 1);
        pedido.addProduto(produto2, 1);
        pedido.setValor(BigDecimal.valueOf(80.00));
        pedido.setQuantidade(2);
        
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(vendedorRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findAllById(produtosIds)).thenReturn(List.of(produto1, produto2));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(any());
        
        assertThrows(InvalidJsonException.class, () -> pedidoService.save(data));
    }
    
}
