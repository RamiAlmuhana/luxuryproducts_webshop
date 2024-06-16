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
public class ProductVariatie {

    @Id
    @GeneratedValue
    private long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PRODUCT_VARIANT_ID", nullable=true)

    private ProductVariant productVariant;

   @ManyToOne
    private Size size;

    private Long quantity_in_stock;

    private Boolean items_in_stock = true;

    private Boolean productionStop = false;



    public ProductVariatie(ProductVariant productVariant, Size size, Long quantity_in_stock) {
        this.productVariant = productVariant;
        this.size = size;
        this.quantity_in_stock = quantity_in_stock;
        this.items_in_stock = stock();
    }

    public Boolean stock(){
        return this.quantity_in_stock > 0;
    }

    public void setQuantity_in_stock(Long quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
        this.items_in_stock = stock();
    }


}
