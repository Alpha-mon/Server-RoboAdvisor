package org.ai.roboadvisor.global.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ai.roboadvisor.global.exception.ErrorCode;
import org.ai.roboadvisor.global.exception.SuccessCode;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final int code;
    private final String message;
    private T data;

    public static ApiResponse<?> success(SuccessCode successCode) {
        return new ApiResponse<>(successCode.getCode(), successCode.getMessage());
    }

    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return new ApiResponse<T>(successCode.getCode(), successCode.getMessage(), data);
    }
    
    public static ApiResponse<?> error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getHttpStatus().value(), errorCode.getMessage());
    }

//    public static ApiResponse error(ErrorCode errorCode, String message) {
//        return new ApiResponse<>(errorCode.getHttpStatusCode(), message);
//    }
}
