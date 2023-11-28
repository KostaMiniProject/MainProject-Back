package kosta.main.bids.entity;

import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "bids")
@NoArgsConstructor
@Getter
@Setter
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

    public enum BidStatus {
        BIDDING, DENIED, SELECTED, DELETED
    }
}
