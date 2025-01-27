package com.academia.loja_accenture.core.api.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class HttpApiErrorMessage {
  private HttpStatus status;
  private String message;
}
