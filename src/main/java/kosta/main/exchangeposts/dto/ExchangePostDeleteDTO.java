package kosta.main.exchangeposts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangePostDeleteDTO {
  private Integer userId; // 삭제 요청하는 사용자의 ID
}
