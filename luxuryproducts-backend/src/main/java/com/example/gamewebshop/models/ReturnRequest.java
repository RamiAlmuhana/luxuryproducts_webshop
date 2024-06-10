package com.example.gamewebshop.models;

import com.example.gamewebshop.models.Product.Product;
import com.example.gamewebshop.models.Product.ProductVariant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequest {
    @GeneratedValue
    @Id
    private Long id;
    
    @OneToOne
    private ProductVariant productVariant;
    private String returnStatus;

    @ManyToOne(cascade = CascadeType.MERGE)
    private CustomUser user;

    public ReturnRequest(Long id, CustomUser user, ProductVariant productVariant, String returnStatus) {
        this.id = id;
        this.user = user;
        this.productVariant = productVariant;
        this.returnStatus = returnStatus;
    }

}