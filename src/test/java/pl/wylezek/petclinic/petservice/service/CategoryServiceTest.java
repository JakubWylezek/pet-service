package pl.wylezek.petclinic.petservice.service;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.wylezek.petclinic.petservice.exceptions.custom.EmptyEntityListException;
import pl.wylezek.petclinic.petservice.exceptions.custom.EntityAlreadyExistException;
import pl.wylezek.petclinic.petservice.exceptions.custom.NotFoundEntityException;
import pl.wylezek.petclinic.petservice.model.Category;
import pl.wylezek.petclinic.petservice.model.Pet;
import pl.wylezek.petclinic.petservice.repository.CategoryRepository;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    private Category category1 = new Category();
    private Category category2 = new Category();
    private Category category3 = new Category();
    private Pet pet1 = new Pet();
    private Pet pet2 = new Pet();

    @BeforeEach
    void setUp() {
        category3 = new Category(3L, "Samoyed", category1, new HashSet<>(), List.of(pet2));
        category1 = new Category(1L, "Dogs", new Category(), Set.of(category3), new LinkedList<>());
        category2 = new Category(2L, "Cats", new Category(), new HashSet<>(), List.of(pet1));
        pet1 = new Pet(1L, "Alfred", 5, category2);
        pet2 = new Pet(2L, "Thor", 4, category3);
    }

    @Test
    void getCategoryByNameShouldCorrectlyReturnCategory() {
        //when
        when(categoryRepository.getCategoryByName(any())).thenReturn(Optional.of(category1));
        //then
        assertEquals(category1, categoryService.getCategoryByName("Dogs"));
        assertEquals(1, categoryService.getCategoryByName("Dogs").getSubCategories().size());
        verify(categoryRepository, times(2)).getCategoryByName(any());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getCategoryByNameShouldThrowNotFoundEntityException() {
        //when
        when(categoryRepository.getCategoryByName(any())).thenReturn(Optional.empty());
        //then
        assertThrows(NotFoundEntityException.class, () -> categoryService.getCategoryByName("Dogs"));
        verify(categoryRepository, times(1)).getCategoryByName(any());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getCategoriesWhereParentIsNullShouldCorrectlyReturnCategories() {
        //when
        when(categoryRepository.getAllByParentIsNull()).thenReturn(List.of(category1, category2));
        //then
        assertEquals(List.of(category1, category2), categoryService.getCategoriesWhereParentIsNull());
        verify(categoryRepository, times(1)).getAllByParentIsNull();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getCategoriesShouldThrowEmptyEntityListException() {
        //when
        when(categoryRepository.getAllByParentIsNull()).thenReturn(List.of());
        //then
        assertThrows(EmptyEntityListException.class, () -> categoryService.getCategoriesWhereParentIsNull());
        verify(categoryRepository, times(1)).getAllByParentIsNull();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void saveCategoryShouldCorrectlyCreateNewCategory() {
        //when
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);
        //then
        assertEquals(category1, categoryService.saveCategory(category1));
        verify(categoryRepository, times(1)).save(any());
        verify(categoryRepository, times(1)).getCategoryByName(any());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void saveCategoryShouldThrowEntityAlreadyExist() {
        //when
        when(categoryRepository.getCategoryByName(any())).thenReturn(Optional.of(category1));
        //then
        assertThrows(EntityAlreadyExistException.class, () -> categoryService.saveCategory(category1));
        verify(categoryRepository, times(1)).getCategoryByName(any());
        verify(categoryRepository, times(0)).save(any(Category.class));
    }

    @Test
    void updateCategoryShouldCorrectlyUpdateCategory() {
        //when
        when(categoryRepository.getCategoryByName(any())).thenReturn(Optional.of(category1));
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);
        //then
        assertEquals(category1, categoryService.updateCategory(category1));
        verify(categoryRepository, times(1)).getCategoryByName(any());
        verify(categoryRepository, times(1)).save(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void updateCategoryShouldThrowNotFoundEntityException() {
        //when
        when(categoryRepository.getCategoryByName(any())).thenReturn(Optional.empty());
        //then
        assertThrows(NotFoundEntityException.class, () -> categoryService.updateCategory(category1));
        verify(categoryRepository, times(1)).getCategoryByName(any());
        verify(categoryRepository, times(0)).save(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void deleteCategoryShouldCorrectlyDeleteCategory() {
        //when
        when(categoryRepository.getCategoryByName(any())).thenReturn(Optional.of(category1));
        //then
        categoryService.deleteCategory(category1);
        verify(categoryRepository, times(1)).getCategoryByName(any());
        verify(categoryRepository, times(1)).delete(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void deleteCategoryShouldThrowNotFoundEntityException() {
        //when
        when(categoryRepository.getCategoryByName(any())).thenReturn(Optional.empty());
        //then
        assertThrows(NotFoundEntityException.class, () -> categoryService.deleteCategory(category1));
        verify(categoryRepository, times(1)).getCategoryByName(any());
        verify(categoryRepository, times(0)).delete(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getCategoriesNamesByParentCategoryNameShouldReturnCorrectCategories() {
        //when
        when(categoryRepository.getCategoryByName(any())).thenReturn(Optional.of(category1));
        //then
        assertEquals(2, categoryService.getCategoriesNamesByParentCategoryName("Dogs").size());
        verify(categoryRepository, times(1)).getCategoryByName(any());
    }
 }
