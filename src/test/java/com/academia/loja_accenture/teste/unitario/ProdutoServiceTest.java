package com.academia.loja_accenture.teste.unitario;

import com.academia.loja_accenture.core.PaginationParams;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.dto.AtualizarProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.ProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private VendedorRepository vendedorRepository;

    private Produto produto;
    private Vendedor vendedor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup do mock de vendedor
        vendedor = new Vendedor();
        vendedor.setId(1L);
        vendedor.setNome("Vendedor Teste");

        // Setup do mock de produto
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição Teste");
        produto.setValor(100.0);
        produto.setVendedor(vendedor);
    }

    @Test
    void testListAll() {
        // Mock do retorno do findAll
        Page<Produto> produtoPage = new PageImpl<>(List.of(produto));
        when(produtoRepository.findAll(any(PageRequest.class))).thenReturn(produtoPage);

        PaginationParams params = new PaginationParams(0, 10);
        List<ProdutoDTO> produtosDTO = produtoService.listAll(params);

        // Verificar se o retorno é o esperado
        assert produtosDTO.size() == 1;
        assert produtosDTO.get(0).getNome().equals("Produto Teste");

        verify(produtoRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testGetById() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoDTO produtoDTO = produtoService.getById(1L);

        assert produtoDTO.getNome().equals("Produto Teste");
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        CadastrarProdutoDTO cadastrarProdutoDTO = new CadastrarProdutoDTO("Produto Novo", "Descrição Produto Novo", 200.0, 1L);
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto savedProduto = produtoService.save(cadastrarProdutoDTO);

        assert savedProduto.getNome().equals("Produto Teste");
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testDelete() {
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        produtoService.delete(1L, 1L);

        verify(produtoRepository, times(1)).delete(produto);
    }

    @Test
    void testUpdate() {
        AtualizarProdutoDTO atualizarProdutoDTO = new AtualizarProdutoDTO("Produto Atualizado", "Descrição Atualizada", 150.0);
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        produtoService.update(1L, 1L, atualizarProdutoDTO);

        assert produto.getNome().equals("Produto Atualizado");
        assert produto.getDescricao().equals("Descrição Atualizada");
        assert produto.getValor() == 150.0;

        verify(produtoRepository, times(1)).save(produto);
    }
}

