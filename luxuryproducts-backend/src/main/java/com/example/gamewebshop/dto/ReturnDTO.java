package com.example.gamewebshop.dto;

import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Product.Product;
import com.fasterxml.jackson.annotation.JsonAlias;


public class ReturnDTO {
    public CustomUser user;
    public Product product;
    public String returnStatus;
    @JsonAlias("user_id")
    public long userID;

    public ReturnDTO(CustomUser user, Product product, String returnStatus, long userID) {
        this.user = user;
        this.product = product;
        this.returnStatus = returnStatus;
        this.userID = userID;


    }





}