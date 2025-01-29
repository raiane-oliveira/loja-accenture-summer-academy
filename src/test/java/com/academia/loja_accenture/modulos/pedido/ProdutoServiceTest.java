package com.academia.loja_accenture.modulos.pedido;

import com.academia.loja_accenture.core.PaginationParams;
import com.academia.loja_accenture.core.exceptions.ProdutoNotFoundException;
import com.academia.loja_accenture.core.exceptions.VendedorNotFoundException;
import com.academia.loja_accenture.factory.MakeProduto;
import com.academia.loja_accenture.factory.MakeVendedor;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.dto.AtualizarProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.ProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import com.academia.loja_accenture.modulos.pedido.service.ProdutoService;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private VendedorRepository vendedorRepository;

    @Test
    void shouldListAllProducts() {
        Vendedor vendedor = MakeVendedor.create();

        PaginationParams params = new PaginationParams(0, 5);
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição Teste");
        produto.setValor(BigDecimal.valueOf(10.0));
        produto.setVendedor(vendedor);

        Page<Produto> produtosPage = new PageImpl<>(List.of(produto));
        when(produtoRepository.findAll(PageRequest.of(params.page(), params.size()))).thenReturn(produtosPage);

        List<ProdutoDTO> produtos = produtoService.listAll(params);

        assertEquals(1, produtos.size());
        assertEquals("Produto Teste", produtos.getFirst().nome());
        verify(produtoRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void shouldGetProductById() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição Teste");
        produto.setValor(BigDecimal.valueOf(10.0));
        produto.setVendedor(MakeVendedor.create());

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoDTO produtoDTO = produtoService.getById(1L);

        assertNotNull(produtoDTO);
        assertEquals("Produto Teste", produtoDTO.nome());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        ProdutoNotFoundException exception = assertThrows(ProdutoNotFoundException.class, () -> produtoService.getById(1L));

        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void shouldSaveProduct() {
        CadastrarProdutoDTO data = new CadastrarProdutoDTO("Produto Teste", "Descrição Teste", BigDecimal.valueOf(10.0), 1L);
        Vendedor vendedor = new Vendedor();
        vendedor.setId(1L);

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome(data.nome());
        produto.setDescricao(data.descricao());
        produto.setValor(data.valor());
        produto.setVendedor(vendedor);

        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto savedProduto = produtoService.save(data);

        assertNotNull(savedProduto);
        assertEquals("Produto Teste", savedProduto.getNome());
        verify(vendedorRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void shouldThrowExceptionWhenSavingProductWithInvalidVendedor() {
        CadastrarProdutoDTO data = new CadastrarProdutoDTO("Produto Teste", "Descrição Teste", BigDecimal.valueOf(10.0), 1L);

        when(vendedorRepository.findById(1L)).thenReturn(Optional.empty());

        VendedorNotFoundException exception = assertThrows(VendedorNotFoundException.class, () -> produtoService.save(data));

        assertEquals("Vendedor não encontrado", exception.getMessage());
    }

    @Test
    void shouldUpdateProduct() {
        Vendedor vendedor = MakeVendedor.create();
        vendedor.setId(1L);

        Produto produto = MakeProduto.create();
        produto.setId(1L);
        produto.setVendedor(vendedor);
        vendedor.setProdutos(List.of(produto));

        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        AtualizarProdutoDTO data = new AtualizarProdutoDTO("Produto atualizado", null, BigDecimal.TEN);
        produtoService.update(vendedor.getId(), produto.getId(), data);

        Optional<Produto> updatedProduct = produtoRepository.findById(produto.getId());

        assertTrue(updatedProduct.isPresent());
        assertEquals("Produto atualizado", updatedProduct.get().getNome());
        assertEquals(BigDecimal.TEN, updatedProduct.get().getValor());
    }

    @Test
    void shouldThrowExceptionWhenUpdateProductWithInvalidVendedor() {
        Vendedor vendedor = MakeVendedor.create();
        vendedor.setId(1L);
        lenient().when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));

        Produto produto = MakeProduto.create();
        produto.setId(1L);
        produto.setVendedor(vendedor);
        vendedor.setProdutos(List.of(produto));

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Vendedor vendedor2 = MakeVendedor.create();
        vendedor2.setId(2L);
        when(vendedorRepository.findById(2L)).thenReturn(Optional.of(vendedor2));

        AtualizarProdutoDTO data = new AtualizarProdutoDTO("Produto atualizado", null, BigDecimal.TEN);

         assertThrows(ProdutoNotFoundException.class,
                () -> produtoService.update(vendedor2.getId(), produto.getId(), data));
    }
}
