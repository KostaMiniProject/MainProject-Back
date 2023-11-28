package kosta.main.bids.dto;
import kosta.main.bids.entity.Bid.BidStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BidListDTO {
    private Integer userId; // 입찰하는 사용자의 ID
    private Integer bidId; // 입찰 ID
    private Integer exchangePostId; // 입찰 대상 게시글 ID
    private BidStatus status; // 입찰 상태
    private List<Integer> itemIds; // 입찰에 사용되는 아이템의 ID 목록
    // 입찰하는 사용자의 Name
    // 대표이미지 URL
    // 입찰 ID
    // item의 name을 ,로 나누고 String로 전송
    //BidStatus가 delete인건 보내지 않기
    //
}
