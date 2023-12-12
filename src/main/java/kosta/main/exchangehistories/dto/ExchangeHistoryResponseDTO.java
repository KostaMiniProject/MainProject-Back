package kosta.main.exchangehistories.dto;

import kosta.main.exchangehistories.entity.ExchangeHistory;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExchangeHistoryResponseDTO {
    private Item item;
    private User user;
    private ExchangePost exchangePost;
    private LocalDateTime createdAt;

    public static ExchangeHistoryResponseDTO of(ExchangeHistory exchangeHistory){
        return new ExchangeHistoryResponseDTO(exchangeHistory.getItem(), exchangeHistory.getExchangeInitiator(), exchangeHistory.getExchangePost(), exchangeHistory.getCreatedAt());
    }

}
