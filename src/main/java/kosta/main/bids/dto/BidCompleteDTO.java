package kosta.main.bids.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 역직렬화? 때문에 Builder로는 해결이 안된다고 합니다.
public class BidCompleteDTO {
    private Integer userId; // 거래 완료를 요청하는 사용자의 ID 검증
}