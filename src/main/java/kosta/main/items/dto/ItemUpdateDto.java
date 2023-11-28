package kosta.main.items.dto;

import jakarta.persistence.*;
import kosta.main.bids.entity.Bid;
import kosta.main.categories.entity.Category;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ItemUpdateDto {
  private String title;
  private String description;
  private Item.ItemStatus itemStatus;
  private String imageUrl;
}
