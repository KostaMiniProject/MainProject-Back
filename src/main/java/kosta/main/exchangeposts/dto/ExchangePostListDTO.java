package kosta.main.exchangeposts.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExchangePostListDTO {
  private final Integer exchangePostId;
  private final Integer userId;
  private final String userName;
  private final Integer itemId;
  private final String itemTitle;
  private final String title;
  private final String address;
  private final String exchangePostStatus;
  private final LocalDateTime created_at;
}
