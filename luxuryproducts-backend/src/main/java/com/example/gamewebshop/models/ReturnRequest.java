package com.example.gamewebshop.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class ReturnRequest {
    @GeneratedValue
    @Id
    private Long id;
    
    @OneToOne
    private Product product;
    private String returnStatus;

    @ManyToOne(cascade = CascadeType.MERGE)
    private CustomUser user;
    public ReturnRequest() {

    }

    public ReturnRequest(Long id, CustomUser user, Product product, String returnStatus) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.returnStatus = returnStatus;
    }

}