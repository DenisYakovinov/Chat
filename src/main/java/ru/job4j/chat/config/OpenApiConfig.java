package ru.job4j.chat.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server defaultServer = new Server();
        defaultServer.setUrl("http://localhost:8080");
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Imaginary Chat Application API").version("1.0.0"))
                .servers(List.of(defaultServer));
    }
}