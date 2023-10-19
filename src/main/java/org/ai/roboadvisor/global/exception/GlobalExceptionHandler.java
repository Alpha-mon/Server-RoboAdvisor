package org.ai.roboadvisor.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;
import org.ai.roboadvisor.global.common.dto.ErrorApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ErrorApiResponse<?>> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ErrorApiResponse.error(e.getErrorCode()));
    }

    /**
     * To handle Tendency type is not valid, in BoardController
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorApiResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (Objects.equals(ex.getRequiredType(), Tendency.class)) {

            // Throw your custom exception or handle it directly here
            CustomException customException = new CustomException(ErrorCode.TENDENCY_INPUT_INVALID);
            return ResponseEntity.status(customException.getErrorCode().getHttpStatus().value())
                    .body(ErrorApiResponse.error(customException.getErrorCode()));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
