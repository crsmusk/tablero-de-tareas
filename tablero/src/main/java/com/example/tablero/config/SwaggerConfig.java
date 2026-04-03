package com.example.tablero.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI tableroOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API de Proyecto Tablero")
                        .description("Documentación interactiva de la API REST para el sistema Tablero de Tareas.")
                        .version("v1.0.0")
                        .contact(new Contact().name("Equipo de Desarrollo").email("equipo@tablero.local"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
