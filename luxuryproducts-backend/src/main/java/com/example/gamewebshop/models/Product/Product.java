package com.example.gamewebshop.models.Product;

import com.example.gamewebshop.models.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String country;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="BRAND_ID", nullable=true)
    private Brand brand;

    private long quantity;

    @OneToMany(cascade=ALL, mappedBy="product")
    private List<ProductVariant> productVariants;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PRODUCT_ID", nullable=true)
    private Category category;


    public Product(Brand brand, String name, String country, Category category) {
        this.brand = brand;
        this.name = name;
        this.country = country;
        this.category = category;
    }

}
