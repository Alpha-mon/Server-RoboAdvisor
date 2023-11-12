package org.ai.roboadvisor.domain.tendency.service;

import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.tendency.dto.TendencyDto;
import org.ai.roboadvisor.domain.tendency.dto.response.TendencyFlaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class FlaskRecommendClient {

    private final WebClient flaskClient;

    @Autowired
    public FlaskRecommendClient(@Qualifier("FlaskClient") WebClient flaskClient) {
        this.flaskClient = flaskClient;
    }

    public TendencyFlaskResponse getStockData(TendencyDto dto) {
        log.info(">> run here");
        try {
            return flaskClient.post()
                    .uri("/recommend_stock")
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(TendencyFlaskResponse.class)
                    .timeout(Duration.ofSeconds(600))
                    .onErrorResume(e -> {
                        log.error("]] Error: Get Recommend stock from Flask Server", e);
                        return Mono.empty();
                    })
                    .block();
        } catch (Exception e) {
            log.error(">> Exception: Get  Recommend stock from Flask Server", e);
            return null;
        }
    }

}
