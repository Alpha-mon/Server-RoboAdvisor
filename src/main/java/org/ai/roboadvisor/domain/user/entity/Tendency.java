package org.ai.roboadvisor.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Tendency {
    LION,
    SNAKE,
    MONKEY,
    SHEEP,
    TYPE_NOT_EXISTS;

    @JsonCreator
    public static Tendency forValue(String value) {
        for (Tendency type : Tendency.values()) {
            if (type.toString().equals(value)) {
                return type;
            }
        }
        return TYPE_NOT_EXISTS;
    }

    @JsonValue
    public String toValue() {
        return this.toString();
    }

}
