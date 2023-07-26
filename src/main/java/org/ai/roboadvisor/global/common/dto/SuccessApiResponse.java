package org.ai.roboadvisor.global.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ai.roboadvisor.global.exception.SuccessCode;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "클라이언트의 요청이 정상적으로 실행되었을 때 응답되는 API Response 양식. " +
        "'data' 값에 응답 데이터가 담겨서 반환된다")
public class SuccessApiResponse<T> {

    @Schema(description = "상태 코드")
    private final int code;
    @Schema(description = "응답 메시지")
    private final String message;
    @Schema(description = "응답 데이터")
    private T data;

    public static SuccessApiResponse<?> success(SuccessCode successCode) {
        return new SuccessApiResponse<>(successCode.getCode(), successCode.getMessage());
    }

    public static <T> SuccessApiResponse<T> success(SuccessCode successCode, T data) {
        return new SuccessApiResponse<T>(successCode.getCode(), successCode.getMessage(), data);
    }

}
