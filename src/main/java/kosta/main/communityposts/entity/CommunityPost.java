package kosta.main.communityposts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kosta.main.comments.entity.Comment;
import kosta.main.communityposts.dto.CommunityPostUpdateDTO;
import kosta.main.global.audit.Auditable;
import kosta.main.likes.entity.Like;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "community_posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@SQLDelete(sql = "UPDATE community_posts SET community_post_status = 3 WHERE community_post_id = ?")
@Where(clause = "community_post_status <> 3")
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

    @Builder.Default
    @Column
    private Integer views = 0;

    @Column(length = 20)
    @Builder.Default
    private CommunityPostStatus communityPostStatus = CommunityPostStatus.PUBLIC;


    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "communityPost", cascade = CascadeType.ALL)
    private List<Like> likePostList = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "communityPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();
  
    @ElementCollection
    @CollectionTable(name = "community_post_images", joinColumns = @JoinColumn(name = "community_post_id"))
    @Column(name = "community_post_image")
    @Builder.Default
    private List<String> images = new ArrayList<>(); // 커뮤니티 게시글의 이미지 리스트

    public void updateCommunityPost(CommunityPostUpdateDTO communityPostUpdateDto) {
        this.content = communityPostUpdateDto.getContent() != null && !communityPostUpdateDto.getTitle().equals(this.content) ? communityPostUpdateDto.getContent() : this.content;
        this.title = communityPostUpdateDto.getTitle() != null && !communityPostUpdateDto.getTitle().equals(this.title) ? communityPostUpdateDto.getTitle() : this.title;
        this.images = communityPostUpdateDto.getImagePaths() != null && !communityPostUpdateDto.getImagePaths().equals(this.images) ? communityPostUpdateDto.getImagePaths() : this.images;
    }

    public void updateLikes(Like like){
        likePostList.add(like);
    }

    public enum CommunityPostStatus {
        PUBLIC, PRIVATE, REPORTED, DELETED
    }

    public void updateCommunityPostStatus(CommunityPost.CommunityPostStatus communityPostStatus) {
        this.communityPostStatus = communityPostStatus;
    }
}
