package kosta.main.items.entity;

import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.bids.entity.Bid;
import kosta.main.categories.entity.Category;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
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
    private ItemStatus itemStatus = ItemStatus.PUBLIC;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;


    public enum ItemStatus {
        PUBLIC, PRIVATE, DELETED
    }

    // 게터와 세터
    // 생략...
}
