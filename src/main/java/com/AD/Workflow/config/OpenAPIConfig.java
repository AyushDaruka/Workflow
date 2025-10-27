package com.AD.Workflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI workflowOpenAPI() {
        Server server = new Server()
                .url("http://localhost:8080")
                .description("Development Server");
        return new OpenAPI()
                .info(new Info()
                        .title("Workflow Application API")
                        .version("1.0.0")
                        .description("API documentation for the Workflow Application"))
                .servers(List.of(server));
    }
}
