package kosta.main.bids.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BidsDTO {
    private Integer userId; //임시
    private List<Integer> itemIds; // 입찰에 사용되는 아이템의 ID 목록
}
