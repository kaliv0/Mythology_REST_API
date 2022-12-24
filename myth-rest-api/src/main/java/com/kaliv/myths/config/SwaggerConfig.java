package com.kaliv.myths.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

//    @Operation(security = { @SecurityRequirement(name = "Authorization") })


    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Spring Boot Myth REST API")
                        .description("Spring Boot Myth REST API Documentation")
                        .version("1.0")
                        .contact(new Contact().name("Kaloyan Ivanov").email("kaloyan.ivanov88@gmail.com"))
                        .description("Myth API Wiki Documentation")
//                        .license(new License().name("License of API").url("API License URL"))
                );
    }
}