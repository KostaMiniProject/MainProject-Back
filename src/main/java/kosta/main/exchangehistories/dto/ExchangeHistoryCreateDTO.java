package kosta.main.exchangehistories.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeHistoryCreateDTO {
  private LocalDateTime exchangeDate; // 교환 날짜
  private Integer exchangePostId; // 교환 게시글 ID
  private Integer selectedBidId; // 선택된 입찰 ID
  // 여기서 Item 객체 대신 Item의 ID를 사용하는 이유는, 실제 생성 시에는 물건의 ID를 통해 데이터베이스에서 물건을 조회할 것이기 때문입니다.
}
