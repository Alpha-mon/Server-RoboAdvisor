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

    // user
    NICKNAME_AVAILABLE(HttpStatus.OK.value(), "사용 가능한 닉네임입니다"),
    LOGIN_SUCCESS(HttpStatus.OK.value(), "로그인에 성공하셨습니다"),

    // community
    POST_UPDATE_SUCCESS(HttpStatus.OK.value(), "게시글 수정이 정상적으로 처리되었습니다"),
    POST_DELETE_SUCCESS(HttpStatus.OK.value(), "게시글 삭제가 정상적으로 처리되었습니다"),
    POST_VIEW_SUCCESS(HttpStatus.OK.value(), "게시글을 조회하는데 성공하셨습니다"),
    BOARD_ALL_VIEW_SUCCESS(HttpStatus.OK.value(), "게시글을 불러오는데 성공하셨습니다"),
    COMMENT_UPDATE_SUCCESS(HttpStatus.OK.value(), "댓글 수정이 정상적으로 처리되었습니다"),
    COMMENT_DELETE_SUCCESS(HttpStatus.OK.value(), "댓글 삭제가 정상적으로 처리되었습니다"),

    NEWS_DATA_GET_SUCCESS(HttpStatus.OK.value(), "뉴스 기사 크롤링에 성공하였습니다"),

    /**
     * 201 CREATED
     */
    // chat
    WELCOME_MESSAGE_CREATED_SUCCESS(HttpStatus.CREATED.value(), "채팅방 입장에 성공하셨습니다"),
    CHAT_CREATED_SUCCESS(HttpStatus.CREATED.value(), "사용자의 채팅 메시지가 정상적으로 처리되었습니다"),
    CHAT_DELETED_SUCCESS(HttpStatus.CREATED.value(), "기존 대화 내용이 정상적으로 삭제되었습니다"),

    // user
    SIGNUP_SUCCESS(HttpStatus.CREATED.value(), "회원가입이 정상적으로 처리되었습니다"),

    // community
    POST_CREATED_SUCCESS(HttpStatus.CREATED.value(), "게시글이 정상적으로 등록되었습니다"),
    COMMENT_CREATED_SUCCESS(HttpStatus.CREATED.value(), "댓글이 정상적으로 등록되었습니다"),

    // tendency
    TENDENCY_UPDATE_SUCCESS(HttpStatus.CREATED.value(), "투자 성향 테스트 결과가 정상적으로 등록되었습니다"),

    // predict
    MARKET_DATA_GET_SUCCESS(HttpStatus.CREATED.value(), "데이터를 불러오는데 성공하였습니다");

    private final int code;
    private final String message;
}
