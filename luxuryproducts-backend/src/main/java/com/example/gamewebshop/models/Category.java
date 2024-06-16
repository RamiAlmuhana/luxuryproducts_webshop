package com.example.gamewebshop.models;

import com.example.gamewebshop.models.Product.Product;
import com.example.gamewebshop.models.Product.Size;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    private long id;

    @Column(columnDefinition="TEXT")
    private String imageUrl;

    @Column(columnDefinition="TEXT")
    private String description;

    private String name;

    @OneToMany(cascade=ALL, mappedBy="category")
    private List<Product> products;


    public Category(String imageUrl, String description, String name) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.name = name;
    }
}
