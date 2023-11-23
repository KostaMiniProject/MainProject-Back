package kosta.main.items.dto;

import jakarta.persistence.*;
import kosta.main.items.entity.Item;
import lombok.Getter;


@Getter
public class ItemDto {

  @Column(length = 255)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(length = 20, nullable = false)
  private Item.ItemStatus itemStatus;

  @Column(columnDefinition = "TEXT")
  private String imageUrl;

}
