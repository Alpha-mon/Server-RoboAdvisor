package org.ai.roboadvisor.domain.predict.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketDataResponse {

    @JsonProperty("negative_news_count")
    private Double negative_news_count;

    @JsonProperty("positive_news_count")
    private Double positive_news_count;

    @JsonProperty("negative_news_examples")
    private List<String> negative_news_examples;

    @JsonProperty("positive_news_examples")
    private List<String> positive_news_examples;

    @JsonProperty("prediction")
    private String prediction;  // positive or negative
}
