package org.ai.roboadvisor.domain.predict.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.predict.dto.response.PriceResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "prices")
public class Price {

    @Id
    private String _id; // this will be automatically set by MongoDB if you don't set it

    @Field("nickname")
    private String nickname;

    @Field("average_prediction")
    private Double average_prediction;

    @Field("Bollinger")
    private Double Bollinger;

    @Field("MACD")
    private Double MACD;

    @Field("MOK")
    private Double MOK;

    @Field("RSI")
    private Double RSI;

    @Field("STCK")
    private Double STCK;

    @Field("VR")
    private Double VR;

    @Field("WR")
    private Double WR;

    @Builder
    private Price(String nickname, Double average_prediction, Double Bollinger, Double MACD,
                  Double MOK, Double RSI, Double STCK, Double VR, Double WR) {
        this.nickname = nickname;
        this.average_prediction = average_prediction;
        this.Bollinger = Bollinger;
        this.MACD = MACD;
        this.MOK = MOK;
        this.RSI = RSI;
        this.STCK = STCK;
        this.VR = VR;
        this.WR = WR;
    }

    public static Price of(String nickname, PriceResponse response) {
        return Price.builder()
                .nickname(nickname)
                .average_prediction(response.getAverage_prediction())
                .Bollinger(response.getBollinger())
                .MACD(response.getMACD())
                .MOK(response.getMOK())
                .RSI(response.getRSI())
                .STCK(response.getSTCK())
                .VR(response.getVR())
                .WR(response.getWR())
                .build();
    }
}
