package com.example.gamewebshop.models;

import jakarta.persistence.*;

@Entity
public class Giftcard {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;
    private int discountAmount;
    private boolean used;


    public Giftcard() {
    }

    public Giftcard(Long id, String code, int discountAmount, boolean used) {
        this.id = id;
        this.code = code;
        this.discountAmount = discountAmount;
        this.used = used;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
