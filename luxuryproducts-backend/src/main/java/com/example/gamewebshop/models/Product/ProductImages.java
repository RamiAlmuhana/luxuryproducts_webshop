package com.example.gamewebshop.models.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ProductImages {
    @Id
    @GeneratedValue
    private long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PRODUCT_VARIANT_ID", nullable=true)
    private ProductVariant productVariant;

    @Column(columnDefinition="TEXT")
    private String imageUrl;

    public ProductImages(ProductVariant productVariant, String imageUrl) {
        this.productVariant = productVariant;
        this.imageUrl = imageUrl;
    }
}
