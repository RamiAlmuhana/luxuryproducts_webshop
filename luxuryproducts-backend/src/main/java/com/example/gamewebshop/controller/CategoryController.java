package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.CategoryDAO;
import com.example.gamewebshop.dto.ProductVariantDTOS.CategoryDTO;
import com.example.gamewebshop.models.Category;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequestMapping("/category")
public class CategoryController {

    private final CategoryDAO categoryDAO;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(this.categoryDAO.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategorieById(@PathVariable Long id){
        return ResponseEntity.ok(this.categoryDAO.getCategoryById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategorieById(@PathVariable String name){
        return ResponseEntity.ok(this.categoryDAO.getCategoryByName(name));
    }


    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody CategoryDTO categoryDTO){
        this.categoryDAO.createCategory(categoryDTO);
        return ResponseEntity.ok("Created a new category named " + categoryDTO.name);
    }
}
