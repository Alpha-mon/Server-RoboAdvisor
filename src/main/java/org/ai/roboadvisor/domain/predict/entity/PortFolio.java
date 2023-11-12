package org.ai.roboadvisor.domain.predict.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.predict.dto.response.PortFolioResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "portfolios")
public class PortFolio {

    @Id
    private String _id; // this will be automatically set by MongoDB if you don't set it

    @Field("nickname")
    private String nickname;

    @Field("stocks")
    private List<String> stocks;

    @Field("predicted_volatilities")
    private Map<String, Double> predicted_volatilities;

    @Field("explanations")
    private List<String> explanations;

    @Field("optimal_weights")
    private List<Double> optimal_weights;

    @Field("risk_contributions")
    private Map<String, Double> risk_contributions;

    @Builder
    private PortFolio(String nickname, List<String> stocks, Map<String, Double> predicted_volatilities,
                      List<String> explanations, List<Double> optimal_weights, Map<String, Double> risk_contributions) {
        this.nickname = nickname;
        this.stocks = stocks;
        this.predicted_volatilities = predicted_volatilities;
        this.explanations = explanations;
        this.optimal_weights = optimal_weights;
        this.risk_contributions = risk_contributions;
    }

    public static PortFolio of(String nickname, PortFolioResponse response) {
        return PortFolio.builder()
                .nickname(nickname)
                .stocks(response.getStocks())
                .predicted_volatilities(response.getPredicted_volatilities())
                .explanations(response.getExplanations())
                .optimal_weights(response.getOptimal_weights())
                .risk_contributions(response.getRisk_contributions())
                .build();
    }
}
