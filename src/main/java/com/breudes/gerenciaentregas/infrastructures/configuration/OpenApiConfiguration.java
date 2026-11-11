package com.breudes.gerenciaentregas.infrastructures.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI gerenciaEntregasOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gerência de Entregas API")
                        .description("API para cadastro e acompanhamento de encomendas e moradores.")
                        .version("1.0.0"));
    }
}