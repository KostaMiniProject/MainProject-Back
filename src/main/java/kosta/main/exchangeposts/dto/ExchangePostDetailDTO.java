package kosta.main.exchangeposts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExchangePostDetailDTO {
  private final Integer exchangePostId;
  private final Integer userId;
  private final String userName;
  private final Integer itemId;
  private final String itemTitle;
  private final String title;
  private final String preferItems;
  private final String address;
  private final String content;
  private final String exchangePostStatus;
}
