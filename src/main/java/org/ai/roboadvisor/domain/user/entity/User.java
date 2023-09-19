package org.ai.roboadvisor.domain.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 인자 없는 기본 생성자 필요
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueNickName", columnNames = {"nickname"}),
                @UniqueConstraint(name = "UniqueEmail", columnNames = {"email"})
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 15)
    private String password;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, length = 30)
    private LocalDate birth;

    @Column(nullable = false, length = 10)
    private String gender;

    @Column(nullable = true, length = 30)
    private String career;

    @Builder
    private User(String email, String password,
                 String nickname, LocalDate birth, String gender, String career) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
        this.career = career;
    }
}
