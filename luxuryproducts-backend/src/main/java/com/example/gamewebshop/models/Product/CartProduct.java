package com.example.gamewebshop.models.Product;

import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Product.Enums.CartProductStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")

public class CartProduct {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Product product;

    @Enumerated(EnumType.STRING)
    private CartProductStatus status;

    private long quantity;

    private long price;

    private String size;

    private long productVariantPrice;

    private String imageUrl;

    private boolean productReturned;

    private String returnStatus;

    private long categoryId;






//    @JsonIgnore
//    @ManyToOne
////    @JoinColumn(name="PLACED_ORDER_ID", nullable=true)
//    private PlacedOrder placedOrder;


    @ManyToOne
    private CustomUser customUser;

    public CartProduct(Product product, long quantity, long price, String size, long productVariantPrice, String imageUrl, CustomUser customUser, CartProductStatus status, long categoryId) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.size = size;
        this.productVariantPrice = productVariantPrice;
        this.imageUrl = imageUrl;
        this.customUser = customUser;
        this.status = status;
        this.categoryId = categoryId;
    }
}
