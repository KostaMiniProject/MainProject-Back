package kosta.main.exchangeposts.entity;

import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_posts")
@NoArgsConstructor
@AllArgsConstructor
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


    public enum ExchangePostStatus {
        EXCHANGING , RESERVATION, COMPLETED, DELETED
    }

    // 게터와 세터
    // 생략...
}
