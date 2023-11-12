package org.ai.roboadvisor.domain.predict.service;

import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.predict.dto.request.PriceRequest;
import org.ai.roboadvisor.domain.predict.dto.request.PortFolioRequest;
import org.ai.roboadvisor.domain.predict.dto.response.PriceResponse;
import org.ai.roboadvisor.domain.predict.dto.response.PortFolioResponse;
import org.ai.roboadvisor.domain.predict.dto.response.MarketDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class FlaskClient {

    private final WebClient flaskClient;

    @Autowired
    public FlaskClient(@Qualifier("FlaskClient") WebClient flaskClient) {
        this.flaskClient = flaskClient;
    }

    public MarketDataResponse getMarketData() {
        try {
            return flaskClient.post()
                    .uri("/predict/market")
                    .retrieve()
                    .bodyToMono(MarketDataResponse.class)
                    .timeout(Duration.ofSeconds(600))
                    .onErrorResume(e -> {
                        log.error("]] Error: Get Market Data from Flask Server", e);
                        return Mono.empty();
                    })
                    .block();
        } catch (Exception e) {
            log.error(">> Exception: Get Market Data from Flask Server", e);
            return null;
        }
    }

    public PriceResponse getPriceData(PriceRequest request) {
        try {
            return flaskClient.post()
                    .uri("/predict/price")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(PriceResponse.class)
                    .timeout(Duration.ofSeconds(600))
                    .onErrorResume(e -> {
                        log.error("]] Error: Get Price Data from Flask Server", e);
                        return Mono.empty();
                    })
                    .block();
        } catch (Exception e) {
            log.error(">> Exception: Get Price Data from Flask Server", e);
            return null;
        }
    }

    public PortFolioResponse getPortFolioData(PortFolioRequest request) {
        try {
            return flaskClient.post()
                    .uri("predict/portfolio")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(PortFolioResponse.class)
                    .timeout(Duration.ofSeconds(600))
                    .onErrorResume(e -> {
                        log.error("]] Error: Get PortFolio data from Flask Server", e);
                        return Mono.empty();
                    })
                    .block();
        } catch (Exception e) {
            log.error(">> Exception: Get PortFolio data from Flask Server", e);
            return null;
        }
    }
}
