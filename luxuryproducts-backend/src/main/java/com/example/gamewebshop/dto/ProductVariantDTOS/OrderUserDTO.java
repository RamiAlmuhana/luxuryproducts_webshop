package com.example.gamewebshop.dto.ProductVariantDTOS;

import com.example.gamewebshop.models.CustomUser;
import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderUserDTO {


   public String name;

   public String infix;

   public String last_name;

   public String zipcode;

   public int houseNumber;

   public String notes;

   public int totalProducts;

   public LocalDateTime orderDate;


   public CustomUser user;

   public List<OrderRetrievalDTO> cartProducts = new ArrayList<>();

   public double totalPrice;
   public double discountedPrice;
   public String promoCode;
}
