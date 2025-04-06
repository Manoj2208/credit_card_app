package com.mk.credit_card_app.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Configuration class for customizing the OpenAPI (Swagger) documentation.
 * <p>
 * This configuration defines API metadata and sets up JWT Bearer authentication
 * for secured endpoints in Swagger UI using OpenAPI 3.0 specification.
 * </p>
 */
@Configuration
public class OpenApi30Configuration {

    /**
     * Customizes the OpenAPI specification for the Credit Card API.
     * Adds API metadata (title, version, description) and configures
     * the JWT bearer authentication scheme.
     *
     * @return an {@link OpenAPI} object configured for the application
     */
    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Credit Card API")
                        .version("1.0.0")
                        .description("API for credit card operations"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
