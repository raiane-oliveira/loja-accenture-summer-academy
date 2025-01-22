package com.academia.loja_accenture.core;

import jakarta.validation.constraints.Min;

public record PaginationParams(@Min(0) int page, @Min(1) int size) {
}
