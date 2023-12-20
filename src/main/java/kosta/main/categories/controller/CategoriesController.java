package kosta.main.categories.controller;


import kosta.main.categories.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoriesController {

    private final CategoryService categoryService;
    @GetMapping
    public ResponseEntity<?> getAllCategory(){
        return new ResponseEntity<>(categoryService.findAllCategory(), HttpStatus.OK);
    }

}
