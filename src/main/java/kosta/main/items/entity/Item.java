package kosta.main.items.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.bids.entity.Bid;
import kosta.main.categories.entity.Category;
import kosta.main.users.entity.User;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@SQLDelete(sql = "UPDATE items SET item_status = 2 WHERE item_id = ? ")
@Where(clause = "item_status <> 2")
public class Item extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer itemId;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "bid_id")
  private Bid bid;

  @JsonIgnore//DTO생기면 지울것
  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @Column(length = 255)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Builder.Default
  @Column(length = 20, nullable = false)
  private ItemStatus itemStatus = ItemStatus.PUBLIC;

  @ElementCollection
  @Builder.Default
  @CollectionTable(name = "item_images", joinColumns = @JoinColumn(name = "item_id"))
  @Column(name = "item_image")
  private List<String> images = new ArrayList<>(); // 아이템의 이미지 리스트

  @Builder.Default
  @Column(length = 20, nullable = false)
  private IsBiding isBiding = IsBiding.NOT_BIDING;

  public enum ItemStatus {
    PUBLIC, PRIVATE, DELETED
  }

  public enum IsBiding {
    NOT_BIDING, BIDING
  }

  public void updateIsBiding(IsBiding isBiding) {
    this.isBiding = isBiding;
  }

  public void updateBid(Bid bid) {
    this.bid = bid;
  }

  public void updateUser(User newUser) {
    this.user = newUser;
  }


  public void itemStatusUpdate(ItemStatus itemStatus) {
    this.itemStatus = itemStatus;
  }
}
