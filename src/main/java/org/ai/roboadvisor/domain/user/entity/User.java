package org.ai.roboadvisor.domain.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 인자 없는 기본 생성자 필요
@Entity
@DynamicUpdate
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    private Tendency tendency;

    @Builder
    private User(String email, String password,
                 String nickname, LocalDate birth, String gender, String career, Tendency tendency) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
        this.career = career;
        this.tendency = tendency;
    }

    public void setTendency(Tendency tendency) {
        this.tendency = tendency;
    }
}
