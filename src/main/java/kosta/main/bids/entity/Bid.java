package kosta.main.bids.entity;

import jakarta.persistence.*;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.global.audit.Auditable;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bids")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@SQLDelete(sql = "UPDATE bids SET status = 3 WHERE bid_id = ? ")
//@Where(clause = "status <> 3")
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

    @Builder.Default
    @Column(length = 10, nullable = false)
    private BidStatus status = BidStatus.BIDDING;

    @Builder.Default
    @OneToMany(mappedBy = "bid", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    public void updateStatus(BidStatus status) {
        this.status = status;
    }

    public void updateCompleteStatus(){
        this.status = BidStatus.COMPLETED;
    }

    public void updateItems(List<Item> items) {
        this.items = items;
    }

    public enum BidStatus {
        BIDDING, DENIED, SELECTED, DELETED, RESERVATION,COMPLETED
    }

    public void removeItem(Item item) {
        this.items.remove(item);
    }
}
