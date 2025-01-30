package com.academia.loja_accenture.config.security;

import com.academia.loja_accenture.modulos.usuario.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final SecurityFilter securityFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf((csrf) -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(auth -> auth.
                   requestMatchers(HttpMethod.GET, "/api/produtos").permitAll()
                   .requestMatchers(HttpMethod.GET, "/api/produtos/{produtoId}").permitAll()
                   .requestMatchers(HttpMethod.GET, "/api/produtos/vendedor/{vendedorId}").permitAll()
                   .requestMatchers(HttpMethod.POST, "/api/produtos").hasRole(UserRole.VENDEDOR.name())
                   .requestMatchers(HttpMethod.PUT, "/api/produtos/{vendedorId}/{produtoId}").hasRole(UserRole.VENDEDOR.name())
                   .requestMatchers(HttpMethod.DELETE, "/api/produtos/{vendedorId}/{produtoId}").hasRole(UserRole.VENDEDOR.name())
                   
                   .requestMatchers(HttpMethod.POST, "/api/pedidos").hasRole(UserRole.CLIENTE.name())
                   
                   .requestMatchers(HttpMethod.POST, "/api/status-pedidos/{pedidoId}/registrar").hasRole(UserRole.VENDEDOR.name())
                   
                   .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                   
                   .requestMatchers(HttpMethod.POST, "/api/vendedores").permitAll()
                   .requestMatchers(HttpMethod.GET, "/api/vendedores/{id}").permitAll()
                   .requestMatchers(HttpMethod.GET, "/api/vendedores").permitAll()
                   .requestMatchers(HttpMethod.PUT, "/api/vendedores/{id}").hasRole(UserRole.VENDEDOR.name())
                   
                   .requestMatchers(HttpMethod.POST, "/api/clientes").permitAll()
                   .requestMatchers(HttpMethod.GET, "/api/clientes").hasRole(UserRole.VENDEDOR.name())
                   .requestMatchers(HttpMethod.PUT, "/api/clientes/{id}").hasRole(UserRole.CLIENTE.name())
                   
                   .requestMatchers(HttpMethod.GET, "/api/estoques").hasRole(UserRole.VENDEDOR.name())
                   .requestMatchers(HttpMethod.GET, "/api/estoques/{id}").hasRole(UserRole.VENDEDOR.name())
                   .requestMatchers(HttpMethod.PATCH, "/api/estoques/{id}/quantidade").hasRole(UserRole.VENDEDOR.name())
                   .requestMatchers(HttpMethod.POST, "/api/estoques").hasRole(UserRole.VENDEDOR.name())
                   
                   .requestMatchers("/swagger-ui.html").permitAll()
                   .requestMatchers("/swagger-ui/**").permitAll()
                   .requestMatchers("/api-docs/**").permitAll()
                   .anyRequest().authenticated()
               )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
