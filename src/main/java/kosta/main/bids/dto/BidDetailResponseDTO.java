package kosta.main.bids.dto;

import kosta.main.bids.entity.Bid.BidStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BidDetailResponseDTO {
    // 입찰 고유 ID
    private Integer bidId;

    // 입찰한 사용자의 고유 ID
    private Integer bidderId;

    // 입찰한 사용자의 닉네임
    private String bidderNickname;

    // 입찰한 사용자의 프로필 이미지 URL
    private String bidderProfileImageUrl;

    // 입찰한 사용자의 주소
    private String bidderAddress;

    // 입찰에 사용된 아이템 목록 (아이템의 ID, 제목, 설명 포함)
    private List<ItemDetails> items;

    // 입찰에 사용된 아이템의 상세 정보를 담는 내부 클래스
    @Builder
    @Getter
    public static class ItemDetails {
        private String title;             // 아이템 제목
        private String description;       // 아이템 설명
        private List<String> imageUrls;   // 아이템 이미지 URL 목록
    }
}
