package kosta.main.exchangeposts.dto;

import kosta.main.exchangeposts.entity.ExchangePost;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExchangePostDetailDTO {
  private Boolean postOwner;
  private String title;
  private String preferItems;
  private String address;
  private String content;
  private UserProfile profile;
  private ItemDetails item;
  private ExchangePost.ExchangePostStatus exchangePostStatus;
  private List<BidDetails> bidList;
  private Boolean isDibs;

  @Getter
  @Builder
  public static class UserProfile {
    private Integer userId;
    private String name;
    private String address;
    private String imageUrl;
    private Double rating; // 평점 필드 추가
  }

  @Getter
  @Builder
  public static class ItemDetails {
    private Integer itemId;
    private String title;
    private String description;
    private List<String> imageUrls;
  }

  @Getter
  @Builder
  public static class BidDetails {
    private Integer bidId;
    private String name;
    private String imageUrl; // 물건 리스트의 첫번째 이미지
    private String items; // 입찰에 사용된 아이템 목록을 문자열로 표현
  }
}
