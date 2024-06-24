package com.example.gamewebshop.models.Product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Brand {
    @Id
    @GeneratedValue
    private long id;

    private String brandName;

    @Column(columnDefinition="TEXT")
    private String description;

    @OneToMany(cascade=ALL, mappedBy="brand")
    private List<Product> product;

    public Brand(String brandName, String description) {
        this.brandName = brandName;
        this.description = description;
    }
}
