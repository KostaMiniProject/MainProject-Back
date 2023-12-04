package kosta.main.bids.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidUpdateDTO {
    //private Integer userId; // 입찰을 수정하는 사용자의 ID
    private List<Integer> itemIds; // 수정할 입찰에 사용될 아이템 ID 목록
}