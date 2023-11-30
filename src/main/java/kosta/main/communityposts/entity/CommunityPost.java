package kosta.main.communityposts.entity;

import jakarta.persistence.*;
import kosta.main.communityposts.dto.CommunityPostUpdateDto;
import kosta.main.global.audit.Auditable;
import kosta.main.likes.entity.Like;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(length = 20)
    @Builder.Default
    private CommunityPostStatus communityPostStatus = CommunityPostStatus.PUBLIC;

    @OneToMany(mappedBy = "communityPost", cascade = CascadeType.REMOVE)
    private List<Like> likePostList = new ArrayList<>();

    public CommunityPost updateCommunityPost( CommunityPostUpdateDto communityPostUpdateDto) {
        this.content = communityPostUpdateDto.getContent() != null ? communityPostUpdateDto.getContent() : this.content;
        this.title = communityPostUpdateDto.getContent() != null ? communityPostUpdateDto.getTitle() : this.title;
        //this.imageUrl = communityPostUpdateDto.getImageUrl() != null ? communityPostUpdateDto.getImageUrl() : this.imageUrl;
        return this;
    }


    public enum CommunityPostStatus {
        PUBLIC, PRIVATE, REPORTED, DELETED
    }

    public void updateCommunityPostStatus(CommunityPost.CommunityPostStatus communityPostStatus) {
        this.communityPostStatus = communityPostStatus;
    }
}
