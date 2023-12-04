package kosta.main.bids.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 역직렬화? 때문에 Builder로는 해결이 안된다고 합니다.
public class BidDeleteDTO {
    private Integer userId; // 현재 삭제 요청을 날리는 userID
}
