package com.example.gamewebshop.models.Product;

import com.example.gamewebshop.models.Product.Enums.Sizes;
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

    @Enumerated(EnumType.STRING)
    private Sizes size;

    public Size(Sizes size) {
        this.size = size;
    }
}
