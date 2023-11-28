package kosta.main.exchangeposts.dto;

import kosta.main.exchangeposts.entity.ExchangePost.ExchangePostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExchangePostResponseDTO {
  private final Integer exchangePostId;
  private final Integer userId;
  private final Integer itemId;
  private final String title;
  private final String preferItems;
  private final String address;
  private final String content;
  private final String exchangePostStatus;
}
