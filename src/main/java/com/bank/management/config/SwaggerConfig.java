package com.bank.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // <- dice a Spring que esta clase es de configuración
public class SwaggerConfig {

    @Bean  // <- expone un objeto que Spring maneja
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestión Bancaria Adri") // título que ves en Swagger
                        .version("1.0") // versión de tu API
                        .description("Documentación interactiva de la API del banco")); // descripción
    }
}
