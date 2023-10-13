package org.ai.roboadvisor.domain.community.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ai.roboadvisor.domain.tendency.entity.Tendency;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 인자 없는 기본 생성자 필요
@Entity
@Table(name = "posts")
public class Post extends BaseTimeEntity {

    // deleteStatus is initialized before the entity is persisted:
    @PrePersist
    private void prePersist() {
        if (deleteStatus == null) {
            deleteStatus = DeleteStatus.F;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "post_tendency", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Tendency tendency;

    @Column(name = "post_nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "post_content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "post_view_count", columnDefinition = "INTEGER DEFAULT 0")
    private Long viewCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_delete_status", nullable = false,
            columnDefinition = "ENUM('T', 'F') DEFAULT 'F'")
    private DeleteStatus deleteStatus;

    // 게시글 UI에서 댓글을 바로 보여주기 위해 FetchType.EAGER 설정
    // (댓글-펼처보기 와 같은 형식이면 LAZY로)
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    @OrderBy("id asc") // 댓글 정렬
    private List<Comment> comments = new ArrayList<>();

    @Builder
    private Post(Tendency tendency, String nickname, String content, Long viewCount, DeleteStatus deleteStatus) {
        this.tendency = tendency;
        this.nickname = nickname;
        this.content = content;
        this.viewCount = viewCount;
        this.deleteStatus = deleteStatus;
    }

    public void setTendency(Tendency tendency) {
        this.tendency = tendency;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public void setDeleteStatus(DeleteStatus deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}
