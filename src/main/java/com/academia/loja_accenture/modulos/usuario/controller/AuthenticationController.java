package com.academia.loja_accenture.modulos.usuario.controller;

import com.academia.loja_accenture.config.security.TokenService;
import com.academia.loja_accenture.modulos.usuario.domain.User;
import com.academia.loja_accenture.modulos.usuario.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação")
public class AuthenticationController {
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  
  @Operation(
      summary = "Realizar login do usuário",
      tags = {"Autenticação"},
      responses = {
          @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Erro ao autenticar usuário")
      }
  )
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
    var auth = authenticationManager.authenticate(usernamePassword);
    
    var token = tokenService.generateToken((User) auth.getPrincipal());
    
    return ResponseEntity.ok().body(new LoginResponseDTO(token));
  }
}
