package org.ai.roboadvisor.domain.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${openai.api.url}")
    private String OPEN_AI_URL;

    @Value("${openai.api.secret-key}")
    private String OPEN_AI_SECRET_KEY;

    @Bean(name = "chatGptWebClient")
    public WebClient chatGptWebClient() {
        return WebClient.builder()
                .baseUrl(OPEN_AI_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + OPEN_AI_SECRET_KEY)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
