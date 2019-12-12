package pl.wylezek.petclinic.petservice.service;

import org.springframework.stereotype.Service;
import pl.wylezek.petclinic.petservice.exceptions.custom.EmptyEntityListException;
import pl.wylezek.petclinic.petservice.exceptions.custom.EntityAlreadyExistException;
import pl.wylezek.petclinic.petservice.exceptions.custom.NotFoundEntityException;
import pl.wylezek.petclinic.petservice.model.Category;
import pl.wylezek.petclinic.petservice.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategoriesWhereParentIsNull() {
        List<Category> categories = this.categoryRepository.getAllByParentIsNull();
        if(categories.isEmpty())
            throw new EmptyEntityListException(Category.class);
        return categories;
    }

    public Category getCategoryByName(String categoryName) {
        return this.categoryRepository.getCategoryByName(categoryName).orElseThrow(() ->
                new NotFoundEntityException(Category.class));
    }

    public Category saveCategory(Category category) {
        try {
            getCategoryByName(category.getName());
            throw new EntityAlreadyExistException(Category.class);
        } catch (NotFoundEntityException e) {
            return this.categoryRepository.save(category);
        }
    }

    public Category updateCategory(Category category) {
        getCategoryByName(category.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Category category) {
        getCategoryByName(category.getName());
        this.categoryRepository.delete(category);
    }

    public Set<String> getCategoriesNamesByParentCategoryName(String categoryName) {
        Category category = getCategoryByName(categoryName);
        return getListCategoriesFromCategoryByParentCategory(category)
                .stream()
                .map(Category::getName)
                .collect(Collectors.toSet());
    }

    private List<Category> getListCategoriesFromCategoryByParentCategory(Category category) {
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        category.getSubCategories()
                .forEach(categoryTemp -> categories.addAll(getListCategoriesFromCategoryByParentCategory(categoryTemp)));
        return categories;
    }
}
