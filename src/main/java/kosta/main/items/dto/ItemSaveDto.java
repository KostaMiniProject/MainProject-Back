package kosta.main.items.dto;

import kosta.main.items.entity.Item;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class ItemSaveDto {
  private String title;
  private String description;
  private Item.ItemStatus itemStatus;
  private String imageUrl;

}
