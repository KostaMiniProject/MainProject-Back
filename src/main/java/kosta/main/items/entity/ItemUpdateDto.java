package kosta.main.items.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemUpdateDto {
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
