package org.ai.roboadvisor.domain.predict.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PriceResponse {

    @JsonProperty("average_prediction")
    private Double average_prediction;

    @JsonProperty("Bollinger")
    private Double Bollinger;

    @JsonProperty("MACD")
    private Double MACD;

    @JsonProperty("MOK")
    private Double MOK;

    @JsonProperty("RSI")
    private Double RSI;

    @JsonProperty("STCK")
    private Double STCK;

    @JsonProperty("VR")
    private Double VR;

    @JsonProperty("WR")
    private Double WR;

    // 아래 값들은 무시하도록.
    @JsonIgnore
    private Double bollinger;
    @JsonIgnore
    private Double macd;
    @JsonIgnore
    private Double mok;
    @JsonIgnore
    private Double rsi;
    @JsonIgnore
    private Double stck;
    @JsonIgnore
    private Double vr;
    @JsonIgnore
    private Double wr;

}
