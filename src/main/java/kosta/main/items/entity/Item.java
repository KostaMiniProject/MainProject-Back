package kosta.main.items.entity;

import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.bids.entity.Bid;
import kosta.main.categories.entity.Category;
import kosta.main.users.entity.User;
import lombok.*;


@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
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

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @Column(length = 255)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(length = 20, nullable = false)
  @Builder.Default
  private ItemStatus itemStatus = ItemStatus.PUBLIC;

  @Column(columnDefinition = "TEXT")
  private String imageUrl;

  @Column(length = 20, nullable = false)
  @Builder.Default
  private IsBiding isBiding = IsBiding.NOT_BIDING;


  public enum ItemStatus {
    PUBLIC, PRIVATE, DELETED
  }

  // 게터와 세터
  // 생략...
  public void itemStatusUpdate(ItemStatus itemStatus) {
    this.itemStatus = itemStatus;
  }

  public enum IsBiding {
    BIDING, NOT_BIDING
  }


//    static public class Builder {
//        private String title;
//        private String description;
//        private String imageUrl;
//        private ItemStatus itemStatus;
//
//        public Builder title(String title) {
//            this.title = title;
//            return this;
//        }
//
//        public Builder description(String description) {
//            this.description = description;
//            return this;
//        }
//
//        public Builder imageUrl(String imageUrl) {
//            this.imageUrl = imageUrl;
//            return this;
//        }
//
//        public Builder itemStatus(ItemStatus itemStatus) {
//            this.itemStatus = itemStatus;
//            return this;
//        }
//        public Item build() {
//            return new Item(title, description, imageUrl);
//        }
//
//    }
}
