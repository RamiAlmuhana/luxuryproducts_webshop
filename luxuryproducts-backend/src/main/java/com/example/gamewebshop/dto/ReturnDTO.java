package com.example.gamewebshop.dto;

import com.example.gamewebshop.dto.ProductVariantDTOS.OrderRetrievalDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Product.Product;
import com.fasterxml.jackson.annotation.JsonAlias;


public class ReturnDTO {
    public CustomUser user;
    public OrderRetrievalDTO cartProduct;
    public String returnStatus;
    @JsonAlias("user_id")
    public long userID;

    public ReturnDTO(CustomUser user, OrderRetrievalDTO cartProduct, String returnStatus, long userID) {
        this.user = user;
        this.cartProduct = cartProduct;
        this.returnStatus = returnStatus;
        this.userID = userID;


    }





}