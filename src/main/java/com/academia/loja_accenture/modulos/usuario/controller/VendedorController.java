package com.academia.loja_accenture.modulos.usuario.controller;

import com.academia.loja_accenture.modulos.usuario.dto.AtualizarVendedorDTO;
import com.academia.loja_accenture.modulos.usuario.dto.RegistrarVendedorDTO;
import com.academia.loja_accenture.modulos.usuario.dto.VendedorDTO;
import com.academia.loja_accenture.modulos.usuario.service.VendedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendedores")
@RequiredArgsConstructor
@Tag(name = "Vendedor", description = "Gerenciamento de vendedores")
public class VendedorController {
    private final VendedorService vendedorService;

    @Operation(
            summary = "Cadastrar um novo vendedor",
            tags = {"Vendedor"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vendedor cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao cadastrar vendedor")
            }
    )
    @PostMapping
    public ResponseEntity<VendedorDTO> cadastrarVendedor(@Valid @RequestBody RegistrarVendedorDTO data) {
        try {
            VendedorDTO novoVendedor = vendedorService.save(data);
            return ResponseEntity.ok(novoVendedor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Listar todos os vendedores",
            tags = {"Vendedor"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vendedores listados com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao listar vendedores")
            }
    )
    @GetMapping
    public ResponseEntity<List<VendedorDTO>> listarVendedores() {
        try {
            List<VendedorDTO> vendedores = vendedorService.listarTodos();
            return ResponseEntity.ok(vendedores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Buscar vendedor por ID",
            tags = {"Vendedor"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vendedor encontrado"),
                    @ApiResponse(responseCode = "404", description = "Vendedor não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Erro ao buscar vendedor")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<VendedorDTO> buscarVendedorPorId(@PathVariable Long id) {
        try {
            VendedorDTO vendedor = vendedorService.getById(id);
            return ResponseEntity.ok(vendedor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Atualizar informações de um vendedor",
            tags = {"Vendedor"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vendedor atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Vendedor não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Erro ao atualizar vendedor")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarVendedor(@PathVariable Long id, @Valid @RequestBody AtualizarVendedorDTO data) {
        try {
            vendedorService.update(id, data);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
