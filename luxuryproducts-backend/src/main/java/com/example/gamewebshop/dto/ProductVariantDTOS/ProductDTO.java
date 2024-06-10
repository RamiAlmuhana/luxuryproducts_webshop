package com.example.gamewebshop.dto.ProductVariantDTOS;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    public String name;
    public String description;
    public Number price;
    public String imgURL;
    @JsonAlias("category_id")
    public long categoryId;
    public String promoCode;
    public double promoDiscount;
    private String promoType;


}
