package org.ai.roboadvisor.domain.tendency.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.tendency.dto.request.TendencyUpdateRequest;
import org.ai.roboadvisor.domain.tendency.service.TendencyService;
import org.ai.roboadvisor.domain.user.entity.Tendency;
import org.ai.roboadvisor.global.common.dto.SuccessApiResponse;
import org.ai.roboadvisor.global.exception.CustomException;
import org.ai.roboadvisor.global.exception.ErrorCode;
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
@Tag(name = "investment-tendency", description = "투자 성향과 관련된 API")
@RestController
@RequestMapping("/api/tendency")
public class TendencyController {

    private final TendencyService tendencyService;

    private final int SUCCESS = 0;
    private final int INVALID_TENDENCY_ERROR = -1;
    private final int INTERNAL_SERVER_ERROR = -100;

    @Operation(summary = "투자 성향 등록", description = "사용자의 투자 성향을 등록한다")
    @ApiResponse(responseCode = "201", description = """
            사용자가 입력한 투자 성향을 등록한다.
                        
            data 값으로 등록된 투자 성향의 결과를 반환한다""",
            content = @Content(schema = @Schema(implementation = SuccessApiResponse.class),
                    examples = @ExampleObject(name = "example",
                            description = "정상 응답 예시",
                            value = """
                                        {
                                            "code": 201,
                                            "message": "투자 성향 테스트 결과가 정상적으로 등록되었습니다",
                                            "data": "LION"
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
    public ResponseEntity<SuccessApiResponse<?>> updateTendency(@RequestBody TendencyUpdateRequest tendencyUpdateRequest) {
        int result = tendencyService.updateTendency(tendencyUpdateRequest);
        if (result == SUCCESS) {
            Tendency updateTendency = tendencyUpdateRequest.getTendency();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(SuccessApiResponse.success(SuccessCode.TENDENCY_UPDATE_SUCCESS, updateTendency));
        } else if (result == INVALID_TENDENCY_ERROR) {
            throw new CustomException(ErrorCode.TENDENCY_INPUT_INVALID);
        } else {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}
