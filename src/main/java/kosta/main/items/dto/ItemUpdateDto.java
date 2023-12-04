package kosta.main.items.dto;

import jakarta.persistence.*;
import kosta.main.bids.entity.Bid;
import kosta.main.categories.entity.Category;
import kosta.main.items.entity.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ItemUpdateDto {
  private String title;
  private String description;
  private Item.ItemStatus itemStatus;
  private List<String> images; // 아이템 이미지 URL 리스트

  public void updateImagePath(List<String> imagePaths){
    this.images = imagePaths;
  }
}
