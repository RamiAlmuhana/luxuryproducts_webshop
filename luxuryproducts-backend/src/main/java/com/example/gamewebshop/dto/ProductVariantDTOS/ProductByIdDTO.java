package com.example.gamewebshop.dto.ProductVariantDTOS;

import com.example.gamewebshop.models.Product.ProductVariant;
import jakarta.persistence.*;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

public class ProductByIdDTO {

    public long id;

    public String name;

    public String country;

    public long quantity;

    @OneToMany(cascade=ALL, mappedBy="product")
    public List<ProductVariant> productVariants;

    public String promoCode;
    public double promoDiscount;
    public String promoType;

    public long categoryId;


}
