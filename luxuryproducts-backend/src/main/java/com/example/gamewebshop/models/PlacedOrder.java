package com.example.gamewebshop.models;

import com.example.gamewebshop.models.Product.CartProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlacedOrder {

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String infix;
    @Column(nullable = true)
    private String last_name;
    @Column(nullable = true)
    private String zipcode;
    @Column(nullable = true)
    private int houseNumber;
    @Column(nullable = true)
    private String notes;
    @Column(nullable = true)
    private int totalProducts;
    @Column(nullable = true)
    private LocalDateTime orderDate;
    @ManyToOne(cascade = CascadeType.MERGE)
    private CustomUser user;

    @OneToMany
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany
    private List<Giftcard> giftcards = new ArrayList<>();

    private double totalPrice;
    @Column(nullable = true)
    private double discountedPrice;
    @Column(nullable = true)
    private String promoCode;
    @Column(nullable = true)
    private String giftCardCode;

    public PlacedOrder(String name, String infix, String last_name, String zipcode, int houseNumber, String notes, CustomUser user, ArrayList<CartProduct> cartProducts) {
        this.name = name;
        this.infix = infix;
        this.last_name = last_name;
        this.zipcode = zipcode;
        this.houseNumber = houseNumber;
        this.notes = notes;
        this.user = user;
        this.cartProducts = cartProducts;
    }
}