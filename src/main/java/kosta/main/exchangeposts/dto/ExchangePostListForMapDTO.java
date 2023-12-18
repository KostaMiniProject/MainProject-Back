package kosta.main.exchangeposts.dto;

import kosta.main.exchangeposts.entity.ExchangePost;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

@Getter
@Builder
public class ExchangePostListForMapDTO { // 교환 게시글 전체의 목록을 Map에게 보내기 위한 DTO
  private final Integer exchangePostId; // 교환 게시글 ID
  private final String title; // 교환 게시글 제목
  private Optional<String> longitude; // X좌표 NULL 가능
  private Optional<String> latitude; // Y좌표 NULL 가능
  private final String exchangePostStatus; // 교환 게시글 상태(EXCHANGING , RESERVATION, COMPLETED, DELETED)
  private final String createdAt; // 교환 게시글 작성일
  private final String imgUrl; // 교환 게시글에 등록된 item의 대표이미지 URㅣ
}
