package com.academia.loja_accenture.config.security;

import com.academia.loja_accenture.modulos.usuario.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
  @Value("${api.security.token.secret}")
  private String secret;
  
  private final ObjectMapper objectMapper = new ObjectMapper();
  
  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      String payload = objectMapper.writeValueAsString(new TokenPayload(user.getId(), user.getRole()));
      
      String token = JWT.create()
          .withIssuer("loja-accenture")
          .withSubject(user.getLogin())
          .withPayload(payload)
          .withExpiresAt(genExpirationDate())
          .sign(algorithm);
          
      return token;
    } catch (JWTCreationException | JsonProcessingException exception) {
      throw new RuntimeException("Erro ao gerar token", exception);
    }
  }
  
  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.require(algorithm)
          .withIssuer("loja-accenture")
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException exception) {
      return "";
    }
  }
  
  private Instant genExpirationDate() {
    return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-03:00")); // Horário brasília
  }
}
