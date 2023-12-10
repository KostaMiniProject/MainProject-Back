package kosta.main.exchangeposts.dto;

import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ExchangePostUpdateResponseDTO {

    private String title;
    private String address;
    private String preferItems;
    private String content;
    private ExchangePostUpdateItemDTO selectedItem;

    public static ExchangePostUpdateResponseDTO from(ExchangePost exchangePost){
        return ExchangePostUpdateResponseDTO.builder()
                .title(exchangePost.getTitle())
                .address(exchangePost.getAddress())
                .preferItems(exchangePost.getPreferItems())
                .content(exchangePost.getContent())
                .selectedItem(ExchangePostUpdateItemDTO.of(exchangePost.getItem()))
                .build();
    }

    @Getter
    @Builder
    public static class ExchangePostUpdateItemDTO{
        private Integer itemId;
        private String title;
        private String description;
        private List<String> images;
        private LocalDateTime createdAt;

        public static ExchangePostUpdateItemDTO of(Item item){
            return ExchangePostUpdateItemDTO.builder()
                    .itemId(item.getItemId())
                    .title(item.getTitle())
                    .description(item.getDescription())
                    .images(item.getImages())
                    .createdAt(item.getCreatedAt())
                    .build();
        }
    }

}
