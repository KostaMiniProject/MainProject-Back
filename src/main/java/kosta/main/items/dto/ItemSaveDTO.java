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

  //  # 조건 : 1~5자 이내
//  @NotEmpty(message = "제목은 필수 항목입니다.")
//  @Size(max = 5, min = 1)
  private String title;

  //  # 조건 : 1~100자 이내
//  @NotEmpty(message = "상세 설명은 필수 항목입니다.")
//  @Size(max = 100, min = 1)
  private String description;

  //카테고리
  private String category;

  private List<String> imageUrl;

  public void updateImageUrl(List<String> imageUrl){
    this.imageUrl = imageUrl;
  }
}
