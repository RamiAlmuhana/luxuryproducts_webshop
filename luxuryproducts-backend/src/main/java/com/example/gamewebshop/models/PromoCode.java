package com.example.gamewebshop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromoCode {
    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private double discount;
    private LocalDateTime expiryDate;
    private LocalDateTime startDate;
    private int maxUsageCount;
    private PromoCodeType type;
    private double minSpendAmount;
    private int usageCount; // New field to track usage count
    private double totalDiscountAmount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    public PromoCode(String code, double discount, LocalDateTime expiryDate, LocalDateTime startDate, int maxUsageCount, PromoCodeType type, Category category, double minSpendAmount) {
        this.code = code;
        this.discount = discount;
        this.expiryDate = expiryDate;
        this.startDate = startDate;
        this.maxUsageCount = maxUsageCount;
        this.type = type;
        this.category = category;
        this.minSpendAmount = minSpendAmount;
        this.usageCount = 0; // Initialize usage count
        this.totalDiscountAmount = 0;
    }

    public enum PromoCodeType {
        FIXED_AMOUNT,
        PERCENTAGE
    }
}
