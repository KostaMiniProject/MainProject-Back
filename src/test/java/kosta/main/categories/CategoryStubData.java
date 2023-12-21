package kosta.main.categories;

import kosta.main.categories.dto.CategoryResponseDTO;
import kosta.main.categories.entity.Category;
import kosta.main.items.ItemStubData;

import java.util.ArrayList;
import java.util.List;

public class CategoryStubData {

    public Category getCategory(){
        ItemStubData itemStubData = new ItemStubData();

        return Category.builder()
                .categoryId(1)
                .categoryName("완구")
                .items(itemStubData.getItems())
                .build();
    }
    public Category getAnotherCategory(){
        ItemStubData itemStubData = new ItemStubData();

        return Category.builder()
                .categoryId(2)
                .categoryName("음식")
                .items(itemStubData.getItems())
                .build();
    }

    public List<CategoryResponseDTO> getCategoryResponseDTOs() {
        List<CategoryResponseDTO> categoryResponseDTOS = new ArrayList<>();
        categoryResponseDTOS.add(CategoryResponseDTO.from(getCategory()));
        categoryResponseDTOS.add(CategoryResponseDTO.from(getAnotherCategory()));
        return categoryResponseDTOS;
    }
}
