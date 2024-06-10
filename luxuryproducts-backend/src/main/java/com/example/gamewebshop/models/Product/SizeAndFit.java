package com.example.gamewebshop.models.Product;

import com.example.gamewebshop.models.Product.Enums.Fit;
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
public class SizeAndFit {

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private Fit fit;

    public SizeAndFit(Fit fit) {
        this.fit = fit;
    }
}
