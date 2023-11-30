package kosta.main.bids.dto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BidCompleteDTO {
    private Integer userId; // 거래 완료를 요청하는 사용자의 ID
}