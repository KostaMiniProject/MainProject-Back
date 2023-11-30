package kosta.main.bids.dto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class BidUpdateResponseDTO {
    private Integer bidId; // 입찰 ID
    private Integer userId; // 사용자 ID
    private Integer exchangePostId; // 교환 게시글 ID
    private String status; // 입찰 상태
    private List<ItemDetails> itemDetails; // 입찰에 사용된 아이템의 상세 정보 리스트

    @Getter
    @Builder
    public static class ItemDetails {
        private Integer itemId; // 아이템 ID
        private String title; // 아이템 제목
        private String description; // 아이템 설명
        private List<String> imageUrls; // 아이템 이미지 URL 리스트
    }
}