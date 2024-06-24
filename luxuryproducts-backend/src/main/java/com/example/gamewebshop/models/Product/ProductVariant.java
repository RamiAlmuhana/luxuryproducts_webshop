package com.example.gamewebshop.models.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductVariant {
    @Id
    @GeneratedValue
    private long id;

    @Column(columnDefinition="TEXT")
    private String description;

    private Long price;

    @ManyToOne
    private Color color;

    @ManyToOne
    private SizeAndFit size_and_fit;


    @OneToMany(cascade=CascadeType.ALL, mappedBy="productVariant")
    private List<ProductImages> productImages;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="productVariant")
    private List<ProductVariatie> productVariatie;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PRODUCT_ID", nullable=true)
    private Product product;


    public ProductVariant(Product product, String description, Color color, Long price, SizeAndFit size_and_fit) {
        this.product = product;
        this.description = description;
        this.color = color;
        this.price = price;
        this.size_and_fit = size_and_fit;
    }
}
