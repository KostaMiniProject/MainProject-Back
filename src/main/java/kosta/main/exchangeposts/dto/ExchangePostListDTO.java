package kosta.main.exchangeposts.dto;

import kosta.main.exchangeposts.entity.ExchangePost;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Getter
@Builder
public class ExchangePostListDTO { // 교환 게시글 전체 목록을 전송하는 DTO
  private final Integer exchangePostId; // 교환 게시글 ID
  private final String title; // 교환 게시글 제목
  private final String preferItem; // 선호아이템
  private final String address; // 거래 희망 주소
  private final String exchangePostStatus; // 교환 게시글 상태(EXCHANGING , RESERVATION, COMPLETED, DELETED)
  private final String createdAt; // 교환 게시글 작성일
  private final String imgUrl; // 교환 게시글에 등록된 item의 대표이미지 URㅣ
  private final Integer bidCount; // 해당 교환 게시글에 등록된 입찰의 갯수를 세서 Integer 값으로 반환

  public static ExchangePostListDTO of(ExchangePost exchangePost,String imageUrl,Integer bidCount){
    return new ExchangePostListDTO(
            exchangePost.getExchangePostId(),
            exchangePost.getTitle(),
            exchangePost.getPreferItems(),
            exchangePost.getAddress(),
            exchangePost.getExchangePostStatus().toString(),
            exchangePost.getCreatedAt().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
            imageUrl,
            bidCount);
  }

  //테스트 용도
  public static ExchangePostListDTO testOf(ExchangePost exchangePost,String imageUrl,Integer bidCount){
    return new ExchangePostListDTO(
            exchangePost.getExchangePostId(),
            exchangePost.getTitle(),
            exchangePost.getPreferItems(),
            exchangePost.getAddress(),
            exchangePost.getExchangePostStatus().toString(),
            LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
            imageUrl,
            bidCount);
  }
}
