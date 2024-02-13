package com.imfreepass.discord.config.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {
	
	@Bean
	public OpenAPI api() {
		Info info = new Info().title("디스코드").version("discord v1").description("디스코드 클론");
		return new OpenAPI().components(new Components()).info(info);
	}
}
