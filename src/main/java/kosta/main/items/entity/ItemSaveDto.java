package kosta.main.items.entity;

import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.bids.entity.Bid;
import kosta.main.categories.entity.Category;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemSaveDto {

  @Id
  private Integer itemId;

  @Column(length = 255)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(length = 20, nullable = false)
  private Item.ItemStatus itemStatus;

  @Column(columnDefinition = "TEXT")
  private String imageUrl;

}
