package com.example.gamewebshop.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ProductDTO {
    private long id;
    public String name;
    public String description;
    public Number price;
    public String imgURL;
    public String specifications;
    public String publisher;
    public String releaseDate;

    @JsonAlias("category_id")
    public long categoryId;

    private String categoryName;
    private String promoCode; // New field
    private double promoDiscount; // New field
    private String promoType; // New field

//    public ProductDTO(String name, String description, Number price, String imgURL, String specifications, String publisher, String releaseDate, long categoryId) {
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.imgURL = imgURL;
//        this.specifications = specifications;
//        this.publisher = publisher;
//        this.releaseDate = releaseDate;
//        this.categoryId = categoryId;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public double getPromoDiscount() {
        return promoDiscount;
    }

    public void setPromoDiscount(double promoDiscount) {
        this.promoDiscount = promoDiscount;
    }

    public String getPromoType() {
        return promoType;
    }

    public void setPromoType(String promoType) {
        this.promoType = promoType;
    }
}
