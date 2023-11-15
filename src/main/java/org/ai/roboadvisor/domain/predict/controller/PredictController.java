package org.ai.roboadvisor.domain.predict.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.predict.dto.request.PriceRequest;
import org.ai.roboadvisor.domain.predict.dto.request.PortFolioRequest;
import org.ai.roboadvisor.domain.predict.dto.response.MarketDataParseResponse;
import org.ai.roboadvisor.domain.predict.dto.response.PriceResponse;
import org.ai.roboadvisor.domain.predict.dto.response.PortFolioResponse;
import org.ai.roboadvisor.domain.predict.dto.response.MarketDataResponse;
import org.ai.roboadvisor.domain.predict.service.PredictService;
import org.ai.roboadvisor.domain.predict.swagger_annotation.predictMarket_CREATED;
import org.ai.roboadvisor.domain.predict.swagger_annotation.predictPortFolio_CREATED;
import org.ai.roboadvisor.domain.predict.swagger_annotation.predictPrice_CREATED;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.ai.roboadvisor.global.swagger_annotation.ApiResponse_Internal_Server_Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "AI-predict", description = "Flask 서버와 연동된, 예측 알고리즘 로직")
@RestController
@RequestMapping("/api/predict")
public class PredictController {

    private final PredictService predictService;

    @Operation(summary = "미래 주식 시장 예측 API", description = """
            뉴스 및 주가 데이터로 미래 주식 시장을 예측한다.
            """)
    @GetMapping("/market")
    @predictMarket_CREATED
    @ApiResponse_Internal_Server_Error
    public ResponseEntity<SuccessApiResponse<MarketDataParseResponse>> predictPrice() {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessApiResponse.success(SuccessCode.MARKET_DATA_GET_SUCCESS,
                        predictService.predictMarket()));
    }

    @Operation(summary = "미래 주가 예측 API", description = """
            과거의 데이터를 이용해 특정 주식 종목의 미래 주가를 예측한다.
                        
            Swagger 문서 하단의 Schemas 중 RequestBody로 'PriceRequest' 를 사용한다.
            """)
    @PostMapping("/price")
    @predictPrice_CREATED
    @ApiResponse_Internal_Server_Error
    public ResponseEntity<SuccessApiResponse<PriceResponse>> predictMarket(@RequestBody PriceRequest priceRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessApiResponse.success(SuccessCode.MARKET_DATA_GET_SUCCESS,
                        predictService.predictPrice(priceRequest)));
    }

    @Operation(summary = "포트폴리오 최적화 연동 API ", description = """
                        
            Swagger 문서 하단의 Schemas 중 RequestBody로 'PortFolioRequest' 를 사용한다.
            """)
    @PostMapping("/portfolio")
    @predictPortFolio_CREATED
    @ApiResponse_Internal_Server_Error
    public ResponseEntity<SuccessApiResponse<PortFolioResponse>> predictPortFolio(@RequestBody PortFolioRequest portFolioRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessApiResponse.success(SuccessCode.MARKET_DATA_GET_SUCCESS,
                        predictService.predictPortFolio(portFolioRequest)));
    }
}
