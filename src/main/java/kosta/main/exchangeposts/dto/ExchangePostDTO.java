package kosta.main.exchangeposts.dto;

import kosta.main.exchangeposts.entity.ExchangePost.ExchangePostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
@Builder
public class ExchangePostDTO {
  private String title; // 교환 게시글 제목
  private Optional<String>  preferItems; // 선호 물건 NULL 가능
  private Optional<String>  address; // 실제 주소명 NULL 가능
  private Optional<String>  longitude; // X좌표 NULL 가능
  private Optional<String>  latitude; // Y좌표 NULL 가능
  private String content; // 상세 설명
  private Integer itemId; // item Id
  private ExchangePostStatus exchangePostStatus; // 교환 게시글 상태
}
