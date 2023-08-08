package org.ai.roboadvisor.domain.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat")
public class Chat {

    @Id
    private String _id; // this will be automatically set by MongoDB if you don't set it

    @Field("email")
    private String email;

    @Field("role")
    private String role;

    @Field("message")
    private String message;

    @Field("time")
    private LocalDateTime time;

    @Builder
    private Chat(String email, String role, String message, LocalDateTime time) {
        this.email = email;
        this.role = role;
        this.message = message;
        this.time = time;
    }

    /**
     * Before save data, add 9 hours to KST timezone
     * MongoDB는 UTC Time zone을 따르기 때문에, KST와 맞추기 위해 9시간 추가
     */
    public void setTimeZone(LocalDateTime time, int value) {
        if (value >= 0) {
            this.time = time.plusHours(value);
        } else {
            this.time = time.minusHours(-value);
        }
    }
}
