package org.ai.roboadvisor.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    /**
     * Swagger springdoc-ui 구성 파일
     */
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("AI Robo-Advisor Swagger")
                .description("AI 로보어드바이저 프로젝트 API 문서")
                .version("1.0.0");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
