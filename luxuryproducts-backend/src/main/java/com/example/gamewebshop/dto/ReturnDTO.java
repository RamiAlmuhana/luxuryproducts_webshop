package com.example.gamewebshop.dto;

import com.example.gamewebshop.dto.ProductVariantDTOS.OrderRetrievalDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Product.Product;
import com.fasterxml.jackson.annotation.JsonAlias;


public class ReturnDTO {
    public long returnRequestId;
    public CustomUser user;
    public OrderRetrievalDTO cartProduct;
    public String returnStatus;
    public String returnReason;
    private String adminReason;
    @JsonAlias("user_id")
    public long userID;

    public ReturnDTO() {
    }


}