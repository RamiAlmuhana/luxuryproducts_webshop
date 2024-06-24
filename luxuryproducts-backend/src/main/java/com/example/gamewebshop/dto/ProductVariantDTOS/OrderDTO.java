// OrderDTO.java
package com.example.gamewebshop.dto.ProductVariantDTOS;

import com.example.gamewebshop.models.CartGiftcard;

import java.util.List;

public class OrderDTO {
    public String name;
    public String infix;
    public String last_name;
    public String zipcode;
    public int houseNumber;
    public String notes;
    public long[] cartProductId;
    public double discountedPrice;
    public String promoCode;
    public String giftCardCode;
    public List<CartGiftcard> cartGiftcards;
}
