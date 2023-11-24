package kosta.main.items.dto;

import kosta.main.items.entity.Item;
import lombok.Getter;


@Getter
public class ItemSaveDto {
  private String title;
  private String description;
  private Item.ItemStatus itemStatus;
  private String imageUrl;

}
