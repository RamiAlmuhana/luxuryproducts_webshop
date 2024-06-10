package com.example.gamewebshop.dao;

import com.example.gamewebshop.Repositorys.CategoryRepository;
import com.example.gamewebshop.dto.ProductVariantDTOS.CategoryDTO;
import com.example.gamewebshop.models.Category;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Component
public class CategoryDAO {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    public void createCategory(CategoryDTO categoryDTO) {
        this.categoryRepository.save(new Category(categoryDTO.imageUrl, categoryDTO.description,categoryDTO.name));
    }

    public Category getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No products found with that category id"
            );
        }
        return category.get();

    }

    public Category getCategoryByName(String name) {
        Optional<Category> category = categoryRepository.findByName(name);

        if (category.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No products found with that category name"
            );
        }
        return category.get();

    }
}
