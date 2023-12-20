package kosta.main.categories.dto;

import kosta.main.categories.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponseDTO {
    private Integer categoryId;
    private String categoryName;

    public static CategoryResponseDTO from(Category category){
        return new CategoryResponseDTO(category.getCategoryId(), category.getCategoryName());
    }
}
