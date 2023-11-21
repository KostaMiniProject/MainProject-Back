package kosta.main.exchangehistories.entity;

import jakarta.persistence.*;
import kosta.main.exchangeposts.entity.ExchangePosts;
import kosta.main.items.entity.Items;

import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_histories")
public class ExchangeHistories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Items item;

    @ManyToOne
    @JoinColumn(name = "exchange_post_id", nullable = false)
    private ExchangePosts exchangePost;

    @Column(nullable = false)
    private LocalDateTime exchangeDate;

    // 게터와 세터
    // 생략...
}
