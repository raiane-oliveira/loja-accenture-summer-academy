package com.academia.loja_accenture.modulos.rastreamento.dto;

import com.academia.loja_accenture.modulos.rastreamento.domain.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarStatusResponseDTO {
    private Long id;
    private StatusEnum status; // Alterado para ENUM, garantindo consistência nos valores
    private LocalDateTime createdAt;
}
