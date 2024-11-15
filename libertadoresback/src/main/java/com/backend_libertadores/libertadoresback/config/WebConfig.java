package com.backend_libertadores.libertadoresback.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
     public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permitir CORS para todos los endpoints
                .allowedOrigins("http://127.0.0.1:5500") // Permitir el dominio de tu frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*") // Permitir todos los encabezados
                .allowCredentials(true); // Permitir el uso de cookies
    }
}
