package com.academia.loja_accenture.modulos.pedido.service;

import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.dto.CadastrarPedidoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.PedidoDTO;
import com.academia.loja_accenture.modulos.pedido.dto.ProdutoDTO;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.repository.ClienteRepository;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;

    public PedidoDTO save(CadastrarPedidoDTO data) {
        Cliente cliente = clienteRepository.findById(data.clienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        Vendedor vendedor = vendedorRepository.findById(data.vendedorId())
                .orElseThrow(() -> new IllegalArgumentException("Vendedor não encontrado"));
        List<Produto> produtos = produtoRepository.findAllById(data.produtosIds());
        if (produtos.isEmpty()) {
            throw new IllegalArgumentException("Nenhum produto encontrado");
        }
        BigDecimal total = produtos.stream()
                .map(Produto::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setProdutos(produtos);
        pedido.setValor(total);
        pedido.setVendedor(vendedor);
        pedido.setQuantidade(produtos.size());
        pedido.setDescricao(data.descricao());
        Pedido savedPedido = pedidoRepository.save(pedido);
        return convertToDTO(savedPedido);
    }

    public PedidoDTO getById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        return convertToDTO(pedido);
    }

    private PedidoDTO convertToDTO(Pedido pedido) {
        return new PedidoDTO(
                pedido.getId(),
                pedido.getDescricao(),
                pedido.getValor(),
                pedido.getQuantidade(),
                pedido.getCliente().getId(),
                pedido.getVendedor().getId(),
                pedido.getProdutos().stream().map(produto ->
                        new ProdutoDTO(
                                produto.getId(),
                                produto.getNome(),
                                produto.getDescricao(),
                                produto.getValor(),
                                produto.getCreatedAt(),
                                produto.getVendedor().getId()
                        )
                ).toList(),
                pedido.getCreatedAt()
        );
    }
}