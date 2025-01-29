package com.academia.loja_accenture.modulos.pedido.service;

import com.academia.loja_accenture.core.exceptions.*;
import com.academia.loja_accenture.core.message_broker.Exchanges;
import com.academia.loja_accenture.core.message_broker.RoutingKeys;
import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import com.academia.loja_accenture.modulos.pedido.dto.*;
import com.academia.loja_accenture.modulos.pedido.repository.PedidoRepository;
import com.academia.loja_accenture.modulos.pedido.repository.ProdutoRepository;
import com.academia.loja_accenture.modulos.usuario.domain.Cliente;
import com.academia.loja_accenture.modulos.usuario.domain.Vendedor;
import com.academia.loja_accenture.modulos.usuario.repository.ClienteRepository;
import com.academia.loja_accenture.modulos.usuario.repository.VendedorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    public PedidoDTO save(CadastrarPedidoDTO data) {
        Cliente cliente = clienteRepository.findById(data.clienteId())
                .orElseThrow(ClienteNotFoundException::new);
        Vendedor vendedor = vendedorRepository.findById(data.vendedorId())
                .orElseThrow(VendedorNotFoundException::new);

        List<Long> produtosIds = data.produtos().stream().map(ProdutoComQuantidadeDTO::id).toList();
        List<Integer> quantidadePorProdutos = data.produtos().stream().map(ProdutoComQuantidadeDTO::quantidade).toList();
        
        List<Produto> produtos = produtoRepository.findAllById(produtosIds);
        if (produtos.isEmpty()) {
            throw new ProdutoNotFoundException();
        }
        
        BigDecimal total = produtos.stream()
                .map(Produto::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int totalProdutosQtd = data.produtos().stream()
                .map(ProdutoComQuantidadeDTO::quantidade)
                .reduce(0, Integer::sum);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setValor(total);
        pedido.setVendedor(vendedor);
        pedido.setQuantidade(totalProdutosQtd);
        pedido.setDescricao(data.descricao());
        
        List<ProdutoComQuantidadeDTO> produtoComQuantidadeDTOs = new ArrayList<>();
        // Adicionar produtos ao pedido
        for (int i = 0; i < produtos.size(); i++) {
            Produto produto = produtos.get(i);
            int quantidade = quantidadePorProdutos.get(i);
            pedido.addProduto(produto, quantidade);
            produtoComQuantidadeDTOs.add(new ProdutoComQuantidadeDTO(produto.getId(), quantidade));
        }
        
        Pedido savedPedido = pedidoRepository.save(pedido);
        
        PedidoDTO pedidoDto = convertToDTO(savedPedido);
        PedidoQueuePayload pedidoPayload = new PedidoQueuePayload(
            pedidoDto.id(),
            pedidoDto.descricao(),
            pedidoDto.valor(),
            pedidoDto.quantidade(),
            pedidoDto.clienteId(),
            pedidoDto.vendedorId(),
            produtoComQuantidadeDTOs,
            pedidoDto.createdAt()
        );
        
        try {
            String pedidoData = objectMapper.writeValueAsString(pedidoPayload);
            amqpTemplate.convertAndSend(Exchanges.PEDIDOS.getName(), RoutingKeys.PEDIDO_REGISTRADO.getName(), pedidoData);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonException("Erro ao processar pedido");
        }
        
        return pedidoDto;
    }

    public PedidoDTO getById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(PedidoNotFoundException::new);
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
                pedido.getPedidoTemProdutos().stream().map(pedidoTemProduto -> {
                    var produto = pedidoTemProduto.getProduto();
                    
                    return new ProdutoDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getValor(),
                        produto.getCreatedAt(),
                        produto.getVendedor().getId()
                    );
                }
                ).toList(),
                pedido.getCreatedAt()
        );
    }
}