package org.ai.roboadvisor.domain.predict.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PortFolioResponse {

    @JsonProperty("stocks")
    private List<String> stocks;

    @JsonProperty("predicted_volatilities")
    private Map<String, Double> predicted_volatilities;

    @JsonProperty("explanations")
    private List<String> explanations;

    @JsonProperty("optimal_weights")
    private List<Double> optimal_weights;

    @JsonProperty("risk_contributions")
    private Map<String, Double> risk_contributions;

}
