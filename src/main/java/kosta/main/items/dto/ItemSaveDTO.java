package kosta.main.items.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
@Builder
public class ItemSaveDTO {

  //  # 조건 : 1~5자 사이
//  @NotNull
//  @Size(max = 5, min = 1)
  private String title;

  //  # 조건 : 1~100자 사이
//  @NotNull
//  @Size(max = 100, min = 1)
  private String description;

  private List<String> imageUrl;

  public void updateImageUrl(List<String> imageUrl){
    this.imageUrl = imageUrl;
  }
}
