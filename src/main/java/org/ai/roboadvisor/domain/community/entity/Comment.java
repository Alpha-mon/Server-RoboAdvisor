package org.ai.roboadvisor.domain.community.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 인자 없는 기본 생성자 필요
@Entity
@Table(name = "comments")
public class Comment extends CommentBaseTimeEntity {

    // deleteStatus is initialized before the entity is persisted:
    @PrePersist
    private void prePersist() {
        if (deleteStatus == null) {
            deleteStatus = DeleteStatus.F;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cmt_id")
    private Long id;

    @Column(name = "cmt_nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "cmt_content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "cmt_delete_status", nullable = false,
            columnDefinition = "ENUM('T', 'F') DEFAULT 'F'")
    private DeleteStatus deleteStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cmt_parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    private Comment(Long id, String nickname, String content,
                    DeleteStatus deleteStatus, Post post) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.deleteStatus = deleteStatus;
        this.post = post;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDeleteStatus(DeleteStatus deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }
}
