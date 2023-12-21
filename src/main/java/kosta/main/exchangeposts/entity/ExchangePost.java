package kosta.main.exchangeposts.entity;

import jakarta.persistence.*;
import kosta.main.bids.entity.Bid;
import kosta.main.exchangehistories.entity.ItemInfo;
import kosta.main.exchangeposts.dto.ExchangePostDTO;
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
@Table(name = "exchange_posts")
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE exchange_posts SET exchange_post_status = 3 WHERE exchange_post_id = ? ")
@Where(clause = "exchange_post_status <> 3")
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

    @Column(length = 255)
    private String longitude; // X 좌표값

    @Column(length = 255)
    private String latitude; // Y 좌표값

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToOne
    @JoinColumn(name = "item_info_id")
    private Item itemInfo;

    @Builder.Default
    @Column(length = 20, nullable = false)
    private ExchangePostStatus exchangePostStatus = ExchangePostStatus.EXCHANGING;

    @Builder.Default
    @OneToMany(mappedBy = "exchangePost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bid> bids = new ArrayList<>();

    public void updateWithBuilder(User user, Item item, ExchangePostDTO dto) {
        this.user = user;
        this.item = item;
        this.title = dto.getTitle() != null ? dto.getTitle() : this.title;
        this.preferItems = dto.getPreferItems() != null ? String.valueOf(dto.getPreferItems()) : this.preferItems;
        this.address = dto.getAddress() != null ? String.valueOf(dto.getAddress()) : this.address;
        this.content = dto.getContent() != null ? dto.getContent() : this.content;
        this.exchangePostStatus = dto.getExchangePostStatus() != null ? dto.getExchangePostStatus() : this.exchangePostStatus;
    }
    public void updateExchangePostStatus(ExchangePostStatus newStatus) {
        this.exchangePostStatus = newStatus;
    }

    public enum ExchangePostStatus {
        EXCHANGING , RESERVATION, COMPLETED, DELETED
    }
    public void addBid(Bid bid){
        bids.add(bid);
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }
}
