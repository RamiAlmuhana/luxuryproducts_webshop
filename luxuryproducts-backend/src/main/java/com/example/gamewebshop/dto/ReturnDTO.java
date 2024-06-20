package com.example.gamewebshop.dto;

import com.example.gamewebshop.dto.ProductVariantDTOS.OrderRetrievalDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Product.Product;
import com.fasterxml.jackson.annotation.JsonAlias;


public class ReturnDTO {
    public CustomUser user;
    public OrderRetrievalDTO cartProduct;
    public String returnStatus;
    public String returnReason;
    private String adminReason;
    @JsonAlias("user_id")
    public long userID;

    public ReturnDTO() {
    }

    public ReturnDTO(CustomUser user, OrderRetrievalDTO cartProduct, String returnStatus, long userID, String returnReason, String adminReason) {
        this.user = user;
        this.cartProduct = cartProduct;
        this.returnStatus = returnStatus;
        this.userID = userID;
        this.returnReason = returnReason;
        this.adminReason = adminReason;
    }





}