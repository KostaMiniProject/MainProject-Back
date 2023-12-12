package kosta.main.bids.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor
public class BidsDTO {
    private List<Integer> itemIds; // 입찰에 사용되는 아이템의 ID 목록
}
