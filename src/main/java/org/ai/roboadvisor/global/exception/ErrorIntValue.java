package org.ai.roboadvisor.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorIntValue {
    SUCCESS(0),

    // user
    EMAIL_ALREADY_EXIST_IN_DB(-1),
    NICKNAME_ALREADY_EXIST_IN_DB(-2),
    USER_NOT_EXISTED(-3),

    // tendency
    INVALID_TENDENCY_ERROR(-10),

    // chat
    TIME_INPUT_INVALID(-20),

    // common
    INTERNAL_SERVER_ERROR(-100);

    private int value;
}
