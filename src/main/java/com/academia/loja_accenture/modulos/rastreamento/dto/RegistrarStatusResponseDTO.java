package com.academia.loja_accenture.modulos.rastreamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RegistrarStatusResponseDTO {
    private Long id;
    private String descricao;
    private LocalDateTime createdAt;
}
