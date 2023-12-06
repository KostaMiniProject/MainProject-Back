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

// @NotBlank : 공백이 아닌 문자가 하나 이상 있는 문자열을 검증
// @NotNull : 값이 null이 아닌지를 검증
// @NotEmpty : 크기가 0이 아닌 객체들을 검증

//  @NotNull
//  @Size(max = 5, min = 1)
  private String title;

//  @NotNull
//  @Size(max = 100, min = 1)
  private String description;

  private List<String> imageUrl;

  public void updateImageUrl(List<String> imageUrl){
    this.imageUrl = imageUrl;
  }
}
