package kosta.main.bids.dto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BidsDto {
    private Integer userId; // 입찰하는 사용자의 ID
    private List<Integer> itemIds; // 입찰에 사용되는 아이템의 ID 목록
}
