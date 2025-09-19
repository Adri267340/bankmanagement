package com.bank.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // Le dice a Spring que esta clase es de configurar
public class SwaggerConfig {

    @Bean  // para exponer un objeto que Spring maneja
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestión Bancaria Adri") // título que ves en Swagger
                        .version("1.0") // versión de tu API
                        .description("Documentación interactiva de la API del banco")); // descripción
    }
}
