package com.example.gamewebshop.Repositorys;

import com.example.gamewebshop.models.Product.ProductVariatie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariatieRepository extends JpaRepository<ProductVariatie, Long> {

}
