package org.ai.roboadvisor.domain.tendency.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.tendency.dto.TendencyUpdateDto;
import org.ai.roboadvisor.domain.tendency.service.TendencyService;
import org.ai.roboadvisor.domain.tendency.swagger_annotation.updateTendency.updateTendency_BAD_REQUEST;
import org.ai.roboadvisor.domain.tendency.swagger_annotation.updateTendency.updateTendency_CREATED;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.ai.roboadvisor.global.swagger_annotation.ApiResponse_Internal_Server_Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "investment-tendency", description = "투자 성향 API")
@RestController
@RequestMapping("/api/tendency")
public class TendencyController {

    private final TendencyService tendencyService;

    @Operation(summary = "투자 성향 등록", description = """
            사용자의 투자 성향을 등록한다
                
            투자 성향 종류: LION(공격투자형), SNAKE(적극투자형), MONKEY(위험중립형), SHEEP(안정추구형)
                        
            Swagger 문서 하단의 Schemas 중 RequestBody로 'TendencyUpdateDto'를 사용한다.
            """)
    @updateTendency_CREATED
    @updateTendency_BAD_REQUEST
    @ApiResponse_Internal_Server_Error
    @PostMapping("/update")
    public ResponseEntity<SuccessApiResponse<TendencyUpdateDto>> updateTendency(@RequestBody TendencyUpdateDto tendencyUpdateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessApiResponse.success(SuccessCode.TENDENCY_UPDATE_SUCCESS, tendencyService.updateTendency(tendencyUpdateDto)));
    }
}
