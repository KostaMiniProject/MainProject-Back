package kosta.main.bids.dto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BidUpdateDTO {
    private Integer userId; // 입찰을 수정하는 사용자의 ID
    private List<Integer> itemIds; // 수정할 입찰에 사용될 아이템 ID 목록
}