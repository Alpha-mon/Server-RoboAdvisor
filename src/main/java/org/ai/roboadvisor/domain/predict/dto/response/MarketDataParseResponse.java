package org.ai.roboadvisor.domain.predict.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketDataParseResponse {

    @JsonProperty("negative_percentage")
    private Double negative_percentage;

    @JsonProperty("positive_percentage")
    private Double positive_percentage;

    @JsonProperty("prediction_result")
    private String prediction_result;

}
