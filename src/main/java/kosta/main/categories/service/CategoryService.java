package kosta.main.categories.service;

import kosta.main.categories.dto.CategoryResponseDTO;
import kosta.main.categories.entity.Category;
import kosta.main.categories.repository.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoriesRepository categoriesRepository;
    public List<CategoryResponseDTO> findAllCategory() {
        return categoriesRepository.findAll().stream().map(CategoryResponseDTO::from).toList();
    }
}
