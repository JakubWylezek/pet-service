package pl.wylezek.petclinic.petservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pl.wylezek.petclinic.petservice.model.Category;
import pl.wylezek.petclinic.petservice.service.CategoryService;

import java.util.List;
import java.util.Set;

@RestController
@EnableWebMvc
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/api/v1/category/{categoryName}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String categoryName) {
        return new ResponseEntity<>(this.categoryService.getCategoryByName(categoryName), HttpStatus.OK);
    }

    @GetMapping(value = "/api/v1/categories")
    public ResponseEntity<List<Category>> getCategoriesWhereParentIsNull() {
        return new ResponseEntity<>(this.categoryService.getCategoriesWhereParentIsNull(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/v1/categories/{categoryName}")
    public ResponseEntity<Set<String>> getCategoriesNamesByParentCategoryName(@PathVariable String categoryName) {
        return new ResponseEntity<>(this.categoryService.getCategoriesNamesByParentCategoryName(categoryName),
                HttpStatus.OK);
    }

    @PostMapping(value = "/api/v1/categories")
    public ResponseEntity<Category> addNewCategory(@RequestBody Category category) {
        return new ResponseEntity<>(this.categoryService.saveCategory(category), HttpStatus.CREATED);
    }

    @PutMapping(value = "/api/v1/categories")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
        return new ResponseEntity<>(this.categoryService.updateCategory(category), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/v1/categories")
    public ResponseEntity deleteCategory(@RequestBody Category category) {
        this.categoryService.deleteCategory(category);
        return ResponseEntity.noContent().build();
    }
}
