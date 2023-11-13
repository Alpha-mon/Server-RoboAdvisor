package org.ai.roboadvisor.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 요청 값입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 내부 오류"),

    // user
    EMAIL_ALREADY_EXIST_IN_DB(HttpStatus.BAD_REQUEST, "U001", "이미 존재하는 이메일입니다"),
    NICKNAME_ALREADY_EXIST_IN_DB(HttpStatus.BAD_REQUEST, "U002", "이미 존재하는 닉네임입니다"),
    USER_NOT_EXISTED(HttpStatus.BAD_REQUEST, "U003", "가입된 사용자의 정보가 존재하지 않습니다"),

    // chat
    TIME_INPUT_INVALID(HttpStatus.BAD_REQUEST, "CH01", "time 형식을 yyyy-MM-dd HH:mm:ss으로 작성해 주세요"),

    // community
    POST_NOT_EXISTED(HttpStatus.BAD_REQUEST, "C001", "요청하신 게시글 id가 존재하지 않습니다"),
    USER_HAS_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "CO02", "사용자에게 권한이 존재하지 않습니다"),
    TENDENCY_NOT_MATCH_BETWEEN_POST_AND_COMMENT(HttpStatus.UNAUTHORIZED,
            "CO03", "게시글과 사용자의 투자 성향이 달라서 댓글을 작성할 수 없습니다"),
    COMMENT_NOT_EXISTED(HttpStatus.BAD_REQUEST, "C004", "요청하신 댓글 id가 존재하지 않습니다"),

    // tendency
    TENDENCY_INPUT_INVALID(HttpStatus.BAD_REQUEST, "TE01", "잘못된 투자 성향 형식이 입력되었습니다"),

    // news
    RECOMMENDED_STOCKS_NOT_EXISTED(HttpStatus.UNAUTHORIZED, "TE01", "추천 주식 종목이 존재하지 않습니다. 투자 성향 테스트를 진행해주세요");

    private final HttpStatus httpStatus;
    private final String type;
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }
}
