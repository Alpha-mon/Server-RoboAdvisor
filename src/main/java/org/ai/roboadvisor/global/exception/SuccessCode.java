package org.ai.roboadvisor.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonFormat(shape = JsonFormat.Shape.OBJECT) // 추가
public enum SuccessCode {

    /**
     * 200 OK
     */
    MESSAGE_CREATED(HttpStatus.OK.value(), "요청이 정상적으로 생성되었습니다");

    /**
     * 201 CREATED
     */

    private final int code;
    private final String message;
}
