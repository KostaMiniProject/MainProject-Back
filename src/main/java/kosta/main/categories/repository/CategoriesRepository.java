package kosta.main.categories.repository;

import kosta.main.categories.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Integer> {


    Optional<Category> findByCategoryName(String categoryName);
}
