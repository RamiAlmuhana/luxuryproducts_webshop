package com.example.gamewebshop.Repositorys;

import com.example.gamewebshop.models.Product.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, Long> {
}
