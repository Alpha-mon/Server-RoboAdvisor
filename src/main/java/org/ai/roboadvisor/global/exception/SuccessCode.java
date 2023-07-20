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

    /**
     * 201 CREATED
     */
    MESSAGE_CREATED(HttpStatus.CREATED.value(), "채팅 메시지가 정상적으로 생성되었습니다");

    private final int code;
    private final String message;
}
