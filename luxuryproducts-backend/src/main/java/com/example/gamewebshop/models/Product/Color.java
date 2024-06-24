package com.example.gamewebshop.models.Product;

import com.example.gamewebshop.models.Product.Enums.Colors;
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
public class Color {
    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private Colors name;

    public Color(Colors name) {
        this.name = name;
    }
}
