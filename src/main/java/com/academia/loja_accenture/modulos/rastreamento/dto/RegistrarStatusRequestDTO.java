package com.academia.loja_accenture.modulos.rastreamento.dto;

import com.academia.loja_accenture.modulos.rastreamento.domain.StatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarStatusRequestDTO {

    @NotNull(message = "O status do pedido é obrigatório.")
    private StatusEnum status; // ENUM para garantir valores padronizados
}
