package kosta.main.bids.entity;

import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "bids")
@NoArgsConstructor
@Getter
public class Bid extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bidId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exchange_post_id")
    private ExchangePost exchangePost;

    @Column(length = 10, nullable = false)
    private BidStatus status = BidStatus.BIDDING;

    @OneToMany(mappedBy = "bid", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Item> items;

    @Builder
    public Bid(Integer bidId, User user, ExchangePost exchangePost, BidStatus status, List<Item> items) {
        this.bidId = bidId;
        this.user = user;
        this.exchangePost = exchangePost;
        this.status = status;
        this.items = items;
    }

    public void updateBid(User user, ExchangePost exchangePost, BidStatus status, List<Item> items) {
        if (user != null) {
            this.user = user;
        }
        if (exchangePost != null) {
            this.exchangePost = exchangePost;
        }
        if (status != null) {
            this.status = status;
        }
        if (items != null && !items.isEmpty()) {
            this.items = items;
        }
        // 참고: items 리스트가 비어있는 경우는 리스트를 유지합니다.
        // 만약 items 리스트를 비우고 싶다면, 별도의 로직을 추가해야 합니다.
    }
    public enum BidStatus {
        BIDDING, DENIED, SELECTED, DELETED
    }
}
