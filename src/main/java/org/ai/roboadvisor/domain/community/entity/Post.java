package org.ai.roboadvisor.domain.community.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 인자 없는 기본 생성자 필요
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "post_type", nullable = false, length = 10)
    private String type;

    @Column(name = "post_nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "post_content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "post_time", nullable = false)
    private LocalDateTime time;

    @Builder
    private Post(String type, String nickname, String content, LocalDateTime time) {
        this.type = type;
        this.nickname = nickname;
        this.content = content;
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
