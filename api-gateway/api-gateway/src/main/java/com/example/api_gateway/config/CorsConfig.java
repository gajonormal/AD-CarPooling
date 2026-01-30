package com.example.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Permite pedidos de QUALQUER origem (Frontend do Leonardo, Postman, etc.)
        corsConfig.addAllowedOriginPattern("*");

        // Permite QUALQUER método (GET, POST, PUT, DELETE, PATCH)
        corsConfig.addAllowedMethod("*");

        // Permite QUALQUER cabeçalho
        corsConfig.addAllowedHeader("*");

        // Permite enviar cookies/credenciais (importante para login futuro)
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}