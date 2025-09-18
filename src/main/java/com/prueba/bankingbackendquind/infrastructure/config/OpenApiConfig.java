package com.prueba.bankingbackendquind.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("banking-backend-quind API")
                        .description("API para gestión de clientes, productos financieros y transacciones")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Santiago Alarcon Vargas")
                                .email("santiagoalarconvargas1@gmail.com")
                                .url("https://github.com/santialar/banking-backend-quind"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
