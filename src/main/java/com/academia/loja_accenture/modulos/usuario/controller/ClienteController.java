package com.academia.loja_accenture.modulos.usuario.controller;

import com.academia.loja_accenture.modulos.usuario.dto.AtualizarClienteDTO;
import com.academia.loja_accenture.modulos.usuario.dto.ClienteDTO;
import com.academia.loja_accenture.modulos.usuario.dto.RegistrarClienteDTO;
import com.academia.loja_accenture.modulos.usuario.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Cliente", description = "Gerenciamento de clientes")
public class ClienteController {
    private final ClienteService clienteService;

    @Operation(
            summary = "Cadastrar um novo cliente",
            tags = {"Cliente"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao cadastrar cliente")
            }
    )
    @PostMapping
    public ResponseEntity<ClienteDTO> cadastrarCliente(@RequestBody @Valid RegistrarClienteDTO data) {
        ClienteDTO novoCliente = clienteService.save(data);
        return ResponseEntity.ok(novoCliente);
    }

    @Operation(
            summary = "Listar todos os clientes",
            tags = {"Cliente"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Clientes listados com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao listar clientes")
            }
    )
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<ClienteDTO> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    @Operation(
            summary = "Buscar cliente por ID",
            tags = {"Cliente"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Erro ao buscar cliente")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarClientePorId(@PathVariable Long id) {
        ClienteDTO cliente = clienteService.getById(id);
        return ResponseEntity.ok(cliente);
    }

    @Operation(
            summary = "Atualizar informações de um cliente",
            tags = {"Cliente"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Erro ao atualizar cliente")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarCliente(@PathVariable Long id, @Valid @RequestBody AtualizarClienteDTO data) {
        clienteService.update(id, data);
        return ResponseEntity.noContent().build();
    }
}
