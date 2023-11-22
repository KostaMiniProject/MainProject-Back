package kosta.main.exchangeposts.dto;

import kosta.main.exchangeposts.entity.ExchangePost.ExchangePostStatus;

public class ExchangePostDTO {
  private String title;
  private String preferItems;
  private String address;
  private String content;
  private Integer userId;
  private Integer itemId;
  private ExchangePostStatus exchangePostStatus;

  // 게터와 세터

  public String getTitle() {
    return title;
  }

  public String getPreferItems() {
    return preferItems;
  }
  public String getAddress() {
    return address;
  }
  public String getContent() {
    return content;
  }
  public Integer getUserId() {
    return userId;
  }
  public Integer getItemId() {
    return itemId;
  }

  public ExchangePostStatus getExchangePostStatus() {
    return exchangePostStatus;
  }

}
