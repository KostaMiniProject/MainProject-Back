package kosta.main.items.dto;

import kosta.main.items.entity.Item;
import kosta.main.users.dto.response.UserItemResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Getter
@AllArgsConstructor
public class ItemDetailResponseDTO {

    private Integer itemId;
    private String title;
    private String description;
    private Item.ItemStatus itemStatus;
    private List<String> images;
    private Item.IsBiding isBiding;
    private String createAt;
    private UserItemResponseDTO user;

    public static ItemDetailResponseDTO of(Item item){
        return new ItemDetailResponseDTO(
                item.getItemId(),
                item.getTitle(),
                item.getDescription(),
                item.getItemStatus(),
                item.getImages(),
                item.getIsBiding(),
                item.getCreatedAt().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                UserItemResponseDTO.of(item.getUser())
        );
    }
}
