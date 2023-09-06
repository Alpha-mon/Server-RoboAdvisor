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
    // chat
    LOAD_CHAT_SUCCESS(HttpStatus.OK.value(), "기존 대화 내용을 정상적으로 불러왔습니다"),
    CHAT_DELETED_SUCCESS(HttpStatus.OK.value(), "기존 대화 내용이 정상적으로 삭제되었습니다"),

    // user
    LOGIN_SUCCESS(HttpStatus.OK.value(), "로그인에 성공하셨습니다"),

    /**
     * 201 CREATED
     */
    // chat
    WELCOME_MESSAGE_CREATED_SUCCESS(HttpStatus.CREATED.value(), "초기 대화 내용이 정상적으로 응답되었습니다"),
    CHAT_CREATED_SUCCESS(HttpStatus.CREATED.value(), "사용자의 채팅 메시지가 정상적으로 처리되었습니다"),

    // user
    SIGNUP_SUCCESS(HttpStatus.CREATED.value(), "회원가입이 정상적으로 처리되었습니다");


    private final int code;
    private final String message;
}
