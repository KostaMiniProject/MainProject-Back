package kosta.main.exchangeposts.entity;

import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exchange_posts")
@NoArgsConstructor
@Getter
public class ExchangePost extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer exchangePostId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(length = 255)
    private String title;

    @Column(length = 100)
    private String preferItems;

    @Column(length = 255)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 20, nullable = false)
    private ExchangePostStatus exchangePostStatus = ExchangePostStatus.EXCHANGING;

    @Builder
    public ExchangePost(Integer exchangePostId, User user, Item item, String title, String preferItems, String address, String content, ExchangePostStatus exchangePostStatus) {
        this.exchangePostId = exchangePostId;
        this.user = user;
        this.item = item;
        this.title = title;
        this.preferItems = preferItems;
        this.address = address;
        this.content = content;
        this.exchangePostStatus = exchangePostStatus;
    }

    public void updateExchangePost(Item item, String title, String preferItems, String address, String content, ExchangePostStatus exchangePostStatus) {
        if (item != null) {
            this.item = item;
        }
        if (title != null) {
            this.title = title;
        }
        if (preferItems != null) {
            this.preferItems = preferItems;
        }
        if (address != null) {
            this.address = address;
        }
        if (content != null) {
            this.content = content;
        }
        if (exchangePostStatus != null) {
            this.exchangePostStatus = exchangePostStatus;
        }
    }


    public enum ExchangePostStatus {
        EXCHANGING , RESERVATION, COMPLETED, DELETED
    }

    // 게터와 세터
    // 생략...
}
