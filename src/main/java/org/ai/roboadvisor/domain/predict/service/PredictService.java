package org.ai.roboadvisor.domain.predict.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.predict.dto.request.PriceRequest;
import org.ai.roboadvisor.domain.predict.dto.request.PortFolioRequest;
import org.ai.roboadvisor.domain.predict.dto.response.PriceResponse;
import org.ai.roboadvisor.domain.predict.dto.response.PortFolioResponse;
import org.ai.roboadvisor.domain.predict.dto.response.MarketDataResponse;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequiredArgsConstructor
@Service
public class PredictService {

    private final FlaskClient flaskClient;

    public MarketDataResponse predictMarket() {
        MarketDataResponse response = flaskClient.getMarketData();
        if (response == null) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return response;
    }


    public PriceResponse predictPrice(@RequestBody PriceRequest priceRequest) {
        PriceResponse response = flaskClient.getPriceData(priceRequest);
        if (response == null) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public PortFolioResponse predictPortFolio(@RequestBody PortFolioRequest portFolioRequest) {
        String stock_list = portFolioRequest.getStock_list().replaceAll("\\s+", ""); // 연속된 모든 공백을 제거
        PortFolioRequest request = new PortFolioRequest(stock_list);
        PortFolioResponse response = flaskClient.getPortFolioData(request);
        if (response == null) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
