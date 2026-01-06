package com.example.springqnaapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		String securitySchemeName = "JWT_Auth";

		Components components = new Components().addSecuritySchemes(
				securitySchemeName,
				new SecurityScheme().name(securitySchemeName)
				                    .type(SecurityScheme.Type.HTTP)
				                    .scheme("bearer")
				                    .bearerFormat("JWT"));

		return new OpenAPI()
				.info(new Info()
						      .title("My Project API")
						      .description("인증 기능이 포함된 Swagger 문서")
						      .version("1.0.0"))
				.components(components);
	}
}