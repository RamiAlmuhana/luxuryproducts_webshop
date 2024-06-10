package com.example.gamewebshop.dto.ProductVariantDTOS;

import java.util.List;


public class OrderDTO{
    public String name;
    public String infix;
    public String last_name;
    public String zipcode;
    public int houseNumber;
    public String notes;
    public List<Long> cartProductId;

    public String promoCode;
    public String giftCardCode;

    public double discountedPrice;

}