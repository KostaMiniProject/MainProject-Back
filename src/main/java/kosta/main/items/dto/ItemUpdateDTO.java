package kosta.main.items.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kosta.main.items.entity.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ItemUpdateDTO {

  //  # 조건 : 1~5자 이내
//  @Size(max = 5, min = 1)
  private String title;

  //  # 조건 : 1~100자 이내
//  @Size(max = 100, min = 1)
  private String description;

  private Item.ItemStatus itemStatus;
  private List<String> images; // 아이템 이미지 URL 리스트

  public void updateImagePath(List<String> imagePaths){
    this.images = imagePaths;
  }
}
