package com.example.gamewebshop.dto.ProductVariantDTOS;

import com.example.gamewebshop.models.Product.Product;

import java.util.List;

public class UpdateProductVariatieDTO {

    public Product product;

    public String productVariantIndex;
    public String description;

    public long price;

    public String color;

    public String sizeAndFit;

    public String imageUrl1;
    public String imageUrl2;


    public List<ProductVariatieDTO> productVariatieDTOS;

}
