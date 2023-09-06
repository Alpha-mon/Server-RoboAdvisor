package org.ai.roboadvisor.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.global.common.dto.ErrorApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ErrorApiResponse<?>> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ErrorApiResponse.error(e.getErrorCode()));
    }
}
