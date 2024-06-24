package com.example.gamewebshop.models;

import com.example.gamewebshop.models.Product.Enums.CartProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CartGiftcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Giftcard giftcard;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private CartProductStatus status;
    @ManyToOne
    private CustomUser user;
}
