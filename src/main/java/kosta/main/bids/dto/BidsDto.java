package kosta.main.bids.dto;
import kosta.main.bids.entity.Bid.BidStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class BidsDto {
    private Integer userId; // 입찰하는 사용자의 ID
    private Integer exchangePostId; // 입찰 대상 게시글 ID
    private BidStatus status; // 입찰 상태
    private List<Integer> itemIds; // 입찰에 사용되는 아이템의 ID 목록
}
