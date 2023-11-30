package kosta.main.bids.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BidListResponseDTO {
    //private Integer userId; // 입찰하는 사용자의 ID
    private Boolean isOwner; // 교환 게시글 작성자인지를 체크
    //private Integer bidId; // 입찰 ID
    //private Integer exchangePostId; // 입찰 대상 게시글 ID
    //private BidStatus status; // 입찰 상태
    private List<ItemDetails> items; // 입찰에 사용되는 아이템의 상세 정보
    @Getter
    @Builder
    public static class ItemDetails {
        private String title; // 아이템 제목
        private String description; // 아이템 설명
        private String imgUrl; // 아이템의 첫 번째 이미지 URL
        private LocalDateTime created_at; //아이템 생성 시간
    }
}
