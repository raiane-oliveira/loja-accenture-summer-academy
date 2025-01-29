package com.academia.loja_accenture.modulos.rastreamento.dto;

import com.academia.loja_accenture.modulos.rastreamento.domain.StatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrarStatusRequestDTO {

    @NotNull(message = "O status do pedido é obrigatório.")
    private StatusEnum status; // ENUM para garantir valores padronizados
}
