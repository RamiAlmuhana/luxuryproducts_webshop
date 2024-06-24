package com.example.gamewebshop.models.Product;

import com.example.gamewebshop.models.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Size {
    @Id
    @GeneratedValue
    private long id;

    private String size;


    private String category;

    public Size(String size, String category) {
        this.size = size;
        this.category = category;
    }
}
