package kosta.main.items.dto;

import kosta.main.items.entity.Item;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class ItemBidDetailsDTO {
        private Integer itemId;
        private String title;             // 아이템 제목
        private String description;       // 아이템 설명
        private List<String> imageUrls;   // 아이템 이미지 URL 목록
        private LocalDateTime createdAt;

    public static ItemBidDetailsDTO of(Item item){
        return ItemBidDetailsDTO.builder()
                .itemId(item.getItemId())
                .title(item.getTitle())
                .description(item.getDescription())
                .imageUrls(item.getImages())
                .createdAt(item.getCreatedAt())
                .build();
    }
}
