package com.weslley.demo_crud.infra.springdoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

    @Configuration
    @OpenAPIDefinition(
            info = @Info(
                    title = "Demo CRUD API",
                    version = "1.0",
                    description = "API para gestão de usuários com autenticação JWT e Refresh Token"
            ),
            // Define que todas as rotas precisam de autenticação por padrão (o cadeado fechado)
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @SecurityScheme(
            name = "bearerAuth",
            type = SecuritySchemeType.HTTP,
            scheme = "bearer",
            bearerFormat = "JWT"
    )
    public class SwaggerConfig {
        // Apenas com as anotações acima, o SpringDoc já faz a mágica!
    }
