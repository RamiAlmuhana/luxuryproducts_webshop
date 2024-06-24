package com.example.gamewebshop.dto.ProductVariantDTOS;

import com.example.gamewebshop.dto.PromocodeDTO;
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

    public long categoryId;


    public List<PromocodeDTO> promocodeDTOS;


}
