package kosta.main.bids.entity;
import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
@NoArgsConstructor
@AllArgsConstructor
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

    public enum BidStatus {
        DENIED, BIDDING, SELECTED, DELETED
    }
}
