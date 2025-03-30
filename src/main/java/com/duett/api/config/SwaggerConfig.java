package com.duett.api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/*
 * Class responsible for managing Swagger which path will open the controller.
 */
@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .components(
                                                new Components()
                                                                .addSecuritySchemes(
                                                                                "bearer",
                                                                                new SecurityScheme()
                                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                                .scheme("bearer")
                                                                                                .bearerFormat("JWT")
                                                                                                .in(SecurityScheme.In.HEADER)
                                                                                                .name("Authorization")))
                                .addSecurityItem(new SecurityRequirement().addList("bearer"))
                                .info(new Info()
                                                .title("API Duett Software")
                                                .version("1.0")

                                                .description("Documentação da API referênte a Duett Software"));
        }

        @Bean
        public GroupedOpenApi apiGroup() {
                return GroupedOpenApi.builder()
                                .group("controllers")
                                .packagesToScan("com.duett.api.controllers")
                                .build();
        }
}
