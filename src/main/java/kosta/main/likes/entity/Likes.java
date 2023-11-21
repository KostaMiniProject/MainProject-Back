package kosta.main.likes.entity;

import jakarta.persistence.*;
import kosta.main.communityposts.entity.CommunityPosts;
import kosta.main.users.entity.Users;

@Entity
@Table(name = "likes")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "community_post_id")
    private CommunityPosts communityPost;

    // 게터와 세터
    // 생략...
}
