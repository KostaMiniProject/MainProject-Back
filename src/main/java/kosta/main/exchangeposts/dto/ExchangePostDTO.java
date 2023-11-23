package kosta.main.exchangeposts.dto;

import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.entity.ExchangePost.ExchangePostStatus;
import lombok.Getter;

@Getter
public class ExchangePostDTO {
  private String title;
  private String preferItems;
  private String address;
  private String content;
  private Integer userId;
  private Integer itemId;
  private ExchangePostStatus exchangePostStatus;
}