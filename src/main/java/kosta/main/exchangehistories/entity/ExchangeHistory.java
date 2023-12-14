package kosta.main.exchangehistories.entity;

import jakarta.persistence.*;
import kosta.main.exchangehistories.dto.ItemHistoryDTO;
import kosta.main.global.audit.Auditable;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "exchange_histories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ExchangeHistory extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer exchangeHistoryId;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "exchange_initiator_id")
    private User exchangeInitiator;  // 교환을 시작한 사용자

    @ManyToOne
    @JoinColumn(name = "exchange_partner_id")
    private User exchangePartner;  // 교환에 응한 사용자

    @ManyToOne
    @JoinColumn(name = "exchange_post_id")
    private ExchangePost exchangePost;

    @ElementCollection
    private List<ItemHistoryDTO> exchangedItems; // 교환한 아이템 목록

    //일단 HistoryEntity로 전부 만들어서 ElementCollection? 아니면 전부 테이블 내에 하나의 컬럼으로 만들어서 사용
    // 게터와 세터
    // 생략...
}
