package kosta.main.global.init;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import kosta.main.categories.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit() {
            createCategoryIfNotExists("패션의류잡화");
            createCategoryIfNotExists("출산유아동");
            createCategoryIfNotExists("주방용품");
            createCategoryIfNotExists("자동차용품");
            createCategoryIfNotExists("음반DVD");
            createCategoryIfNotExists("완구취미");
            createCategoryIfNotExists("식품");
            createCategoryIfNotExists("스포츠레저");
            createCategoryIfNotExists("생활용품");
            createCategoryIfNotExists("뷰티");
            createCategoryIfNotExists("반려애완용품");
            createCategoryIfNotExists("문구오피스");
            createCategoryIfNotExists("도서");
            createCategoryIfNotExists("가전디지털");
            createCategoryIfNotExists("가구홈데코");
            createCategoryIfNotExists("기타");
        }

        private void createCategoryIfNotExists(String categoryName) {
            TypedQuery<Category> query = em.createQuery("SELECT c FROM Category c WHERE c.categoryName = :categoryName", Category.class);
            query.setParameter("categoryName", categoryName);
            List<Category> categories = query.getResultList();

            if (categories.isEmpty()) {
                Category category = new Category();
                category.setCategoryName(categoryName);
                em.persist(category);
            }
        }
    }
}