package kosta.main.exchangeposts.entity;

import jakarta.persistence.*;
import kosta.main.bids.entity.Bid;
import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.global.audit.Auditable;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "exchangePost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bid> bids = new ArrayList<>();

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
    public void updateWithBuilder(User user, Item item, ExchangePostDTO dto) {
        this.user = user;
        this.item = item;
        this.title = dto.getTitle() != null ? dto.getTitle() : this.title;
        this.preferItems = dto.getPreferItems() != null ? dto.getPreferItems() : this.preferItems;
        this.address = dto.getAddress() != null ? dto.getAddress() : this.address;
        this.content = dto.getContent() != null ? dto.getContent() : this.content;
        this.exchangePostStatus = dto.getExchangePostStatus() != null ? dto.getExchangePostStatus() : this.exchangePostStatus;
    }

    public enum ExchangePostStatus {
        EXCHANGING , RESERVATION, COMPLETED, DELETED
    }
}
