package kosta.main.bids.dto;

import kosta.main.bids.entity.Bid;
import kosta.main.items.dto.ItemBidDetailsDTO;
import kosta.main.users.dto.UserBidResponseDTO;
import kosta.main.users.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BidDetailResponseDTO {
    //private Integer bidId; // 입찰 고유 ID
    private Boolean isOwner; // 교환 게시글 작성자인지를 체크
    //private Integer bidderId;// 입찰한 사용자의 고유 ID
    private UserBidResponseDTO profile;

    private List<ItemBidDetailsDTO> items;// 입찰에 사용된 아이템 목록 (아이템의 ID, 제목, 설명 포함)

    public static BidDetailResponseDTO of(Bid bid,boolean isOwner){
        return BidDetailResponseDTO.builder()
                .isOwner(isOwner)
                .profile(UserBidResponseDTO.from(bid.getUser()))
                .items(bid.getItems().stream().map(ItemBidDetailsDTO::of).toList())
                .build();
    }

}
