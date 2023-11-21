package kosta.main.exchangehistories.entity;

import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_histories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExchangeHistory extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer exchangeHistoryId;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "exchange_post_id", nullable = false)
    private ExchangePost exchangePost;

    @Column
    private LocalDateTime exchangeDate;

    // 게터와 세터
    // 생략...
}
