package kosta.main.items.entity;

import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemDeleteDto {
  @Id
  private Integer itemId;

  @Column(length = 20, nullable = false)
  private Item.ItemStatus itemStatus;
}
