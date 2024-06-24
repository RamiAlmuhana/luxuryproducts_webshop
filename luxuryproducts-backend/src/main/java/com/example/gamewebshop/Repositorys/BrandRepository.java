package com.example.gamewebshop.Repositorys;

import com.example.gamewebshop.models.Product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository  extends JpaRepository<Brand, Long> {

    Optional<Brand> findByBrandName(String name);




}
