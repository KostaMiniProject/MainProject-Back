package kosta.main.exchangehistories.dto;

import kosta.main.exchangehistories.entity.ExchangeHistory;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ExchangeHistoryResponseDto {
    private Item item;
    private User user;
    private ExchangePost exchangePost;
    private LocalDateTime exchangeDate;

    public static ExchangeHistoryResponseDto of(ExchangeHistory exchangeHistory){
        return new ExchangeHistoryResponseDto(exchangeHistory.getItem(), exchangeHistory.getUser(), exchangeHistory.getExchangePost(), exchangeHistory.getExchangeDate());
    }

}
