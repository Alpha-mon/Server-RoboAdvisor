package org.ai.roboadvisor.domain.tendency.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.tendency.dto.TendencyUpdateDto;
import org.ai.roboadvisor.domain.tendency.service.TendencyService;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.SuccessCode;
import org.ai.roboadvisor.global.swagger_annotation.ApiResponse_Internal_Server_Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "investment-tendency", description = "투자 성향 API")
@RestController
@RequestMapping("/api/tendency")
public class TendencyController {

    private final TendencyService tendencyService;

    @Operation(summary = "투자 성향 등록", description = "사용자의 투자 성향을 등록한다")
    @ApiResponse(responseCode = "201", description = """
            사용자가 입력한 투자 성향을 등록한다.
                        
            투자 성향 종류: LION(공격투자형), SNAKE(적극투자형), MONKEY(위험중립형), SHEEP(안정추구형)
                        
            data 값으로 등록된 투자 성향의 결과를 반환한다""",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "정상 응답 예시",
                            value = """
                                        {
                                            "code": 201,
                                            "message": "투자 성향 테스트 결과가 정상적으로 등록되었습니다",
                                            "data": {
                                                "nickname": "testUser",
                                                "tendency": "SHEEP"
                                            }
                                        }
                                    """
                    )))
    @ApiResponse(responseCode = "400", description = "잘못된 투자 성향이 입력된 경우",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "잘못된 투자 성향이 입력된 경우 예시",
                            value = """
                                       {
                                            "code": 400,
                                            "message": "잘못된 투자 성향 형식이 입력되었습니다",
                                            "data": null
                                       }
                                    """
                    )))
    @ApiResponse_Internal_Server_Error
    @PostMapping("/update")
    public ResponseEntity<SuccessApiResponse<TendencyUpdateDto>> updateTendency(@RequestBody TendencyUpdateDto tendencyUpdateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessApiResponse.success(SuccessCode.TENDENCY_UPDATE_SUCCESS, tendencyService.updateTendency(tendencyUpdateDto)));
    }

}
