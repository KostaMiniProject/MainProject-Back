package kosta.main.communityposts.entity;

import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.communityposts.dto.CommunityPostUpdateDto;
import kosta.main.users.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import kosta.main.global.audit.Auditable;
import kosta.main.users.entity.User;
import lombok.*;

@Entity
@Table(name = "community_posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CommunityPost extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer communityPostId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private Integer views;

    @Builder.Default
    @Column(length = 20)
    private CommunityPostStatus communityPostStatus = CommunityPostStatus.PUBLIC;

    @Column(length = 255)
    private String imageUrl;

    public CommunityPost updateCommunityPost( CommunityPostUpdateDto communityPostUpdateDto) {
        this.content = communityPostUpdateDto.getContent() != null ? communityPostUpdateDto.getContent() : this.content;
        this.title = communityPostUpdateDto.getContent() != null ? communityPostUpdateDto.getTitle() : this.title;
        this.imageUrl = communityPostUpdateDto.getImageUrl() != null ? communityPostUpdateDto.getImageUrl() : this.imageUrl;
        return this;
    }


    public enum CommunityPostStatus {
        PUBLIC, PRIVATE, REPORTED, DELETED
    }

    public void updateCommunityPostStatus(CommunityPost.CommunityPostStatus communityPostStatus) {
        this.communityPostStatus = communityPostStatus;
    }
}
}
