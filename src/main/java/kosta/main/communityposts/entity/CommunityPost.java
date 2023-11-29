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

import java.util.List;

@Entity
@Table(name = "community_posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
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
    private CommunityPostStatus communityPostStatus = CommunityPostStatus.PUBLIC;

    @ElementCollection
    @CollectionTable(name = "community_post_images", joinColumns = @JoinColumn(name = "community_post_id"))
    @Column(name = "community_post_image")
    private List<String> images = new ArrayList<>(); // 커뮤니티 게시글의 이미지 리스트

    @OneToMany(mappedBy = "communityPost", cascade = CascadeType.REMOVE)
    private List<Like> likePostList = new ArrayList<>();

    private Integer likeCount;
    @Builder
    public CommunityPost(Integer communityPostId, User user, String title, String content, List<String> images) {
        this.communityPostId = communityPostId;
        this.user = user;
        this.title = title;
        this.content = content;
        this.images = images;
        this.likeCount = 0;
    }

    public CommunityPost updateCommunityPost( CommunityPostUpdateDto communityPostUpdateDto) {
        this.content = communityPostUpdateDto.getContent() != null ? communityPostUpdateDto.getContent() : this.content;
        this.title = communityPostUpdateDto.getContent() != null ? communityPostUpdateDto.getTitle() : this.title;
        this.images = communityPostUpdateDto.getImagePaths() != null ? communityPostUpdateDto.getImagePaths() : this.images;
        return this;
    }


    public enum CommunityPostStatus {
        PUBLIC, PRIVATE, REPORTED, DELETED
    }

    public void updateCommunityPostStatus(CommunityPost.CommunityPostStatus communityPostStatus) {
        this.communityPostStatus = communityPostStatus;
    }

    public void likePostUp() {
        this.likeCount++;
    }

    public void likePostDown() {
        this.likeCount--;
    }
}
