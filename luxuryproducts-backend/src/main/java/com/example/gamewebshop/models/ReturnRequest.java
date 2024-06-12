package com.example.gamewebshop.models;

import com.example.gamewebshop.models.Product.CartProduct;
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
    private CartProduct cartProduct;
    private String returnStatus;

    @ManyToOne(cascade = CascadeType.MERGE)
    private CustomUser user;

    public ReturnRequest(Long id, CustomUser user, CartProduct cartProduct, String returnStatus) {
        this.id = id;
        this.user = user;
        this.cartProduct = cartProduct;
        this.returnStatus = returnStatus;
    }

}