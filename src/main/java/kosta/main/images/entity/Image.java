package kosta.main.images.entity;

import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.users.entity.User;
import kosta.main.items.entity.Item;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.chats.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Image extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer imageId;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user; // 사용자 엔터티 참조

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item; // 아이템 엔터티 참조

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "community_post_id")
  private CommunityPost communityPost; // 커뮤니티 게시물 엔터티 참조

  @ManyToOne
  @JoinColumn(name = "chat_id")
  private Chat chat; // 채팅 엔터티 참조

  @Column(columnDefinition = "TEXT")
  private String imageUrl; // 이미지 URL
}
