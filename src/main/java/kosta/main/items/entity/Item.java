package kosta.main.items.entity;

import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.bids.entity.Bid;
import kosta.main.categories.entity.Category;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@Getter

public class Item extends Auditable {
    @Builder
    public Item(String title, String description, String imageUrl, ItemStatus itemStatus) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.itemStatus = itemStatus;
    }



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
    private ItemStatus itemStatus = ItemStatus.PUBLIC;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;


    public enum ItemStatus {
        PUBLIC, PRIVATE, DELETED
    }

    // 게터와 세터
    // 생략...

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
