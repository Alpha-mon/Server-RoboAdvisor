package org.ai.roboadvisor.global.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.ai.roboadvisor.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "클라이언트의 요청이 정상적으로 실행되지 않았을 때 응답되는 API Response 양식. " +
        "보통 'data' 값으로 null이 포함된다")
public class ErrorApiResponse<T> {

    @Schema(description = "상태 코드")
    private final int code;
    @Schema(description = "응답 메시지")
    private final String message;
    @Schema(description = "응답 데이터")
    private T data;

    public static ErrorApiResponse<?> error(ErrorCode errorCode) {
        return new ErrorApiResponse<>(errorCode.getHttpStatus().value(), errorCode.getMessage());
    }

    /*public static ErrorApiResponse<?> error(ErrorCode errorCode, String message) {
        return new ErrorApiResponse<>(errorCode.getHttpStatus().value(), message);
    }*/
}
