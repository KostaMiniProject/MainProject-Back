package kosta.main.items.dto;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
@Builder
public class ItemSaveDTO {
  private String title;
  private String description;
  private List<String> imageUrl;

  public void updateImageUrl(List<String> imageUrl){
    this.imageUrl = imageUrl;
  }
}
