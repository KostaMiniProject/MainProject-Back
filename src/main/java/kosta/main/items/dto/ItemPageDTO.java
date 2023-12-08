package kosta.main.items.dto;


import kosta.main.items.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@Builder
public class ItemPageDTO {
  private Integer itemId;
  private String title;
  private String description;
  private Item.ItemStatus itemStatus;
  private String images; // 가장 첫번째 아이템 이미지 URL 1개만
  private LocalDateTime localDateTime;

  public void updateImagePath(String imagePaths){
    this.images = imagePaths;
  }

  public static ItemPageDTO from (Item item){ //from : 엔티티를 DTO로 생성
    return new ItemPageDTO(
            item.getItemId(),
        item.getTitle(),
        item.getDescription(),
        item.getItemStatus(),
        item.getImages().get(0),
            item.getCreatedAt()
    );
  }
}
