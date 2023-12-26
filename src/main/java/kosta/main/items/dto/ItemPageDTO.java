package kosta.main.items.dto;


import kosta.main.items.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;


@Getter
@AllArgsConstructor
@Builder
public class ItemPageDTO {
  private Integer itemId;
  private String title;
  private String description;
  private Item.ItemStatus itemStatus;
  private Item.IsBiding isBiding;
  private List<String> images; // 가장 첫번째 아이템 이미지 URL 1개만
  private String crateAt;

  public void updateImagePath(String imagePaths){
    this.images.add(imagePaths);
  }

  public static ItemPageDTO from(Item item) { //from : 엔티티를 DTO로 생성

    return new ItemPageDTO(
            item.getItemId(),
            item.getTitle(),
            item.getDescription(),
            item.getItemStatus(),
            item.getIsBiding(),
            item.getImages(),
            item.getCreatedAt().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
    );
  }
}
