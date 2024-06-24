package com.example.gamewebshop.Repositorys;

import com.example.gamewebshop.models.Product.CartProduct;
import com.example.gamewebshop.models.Product.Enums.CartProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    Optional<List<CartProduct>> findByCustomUserIdAndStatus(Long id, CartProductStatus cartProductStatus);
    Optional<List<CartProduct>> findAllByProductIdAndStatus(Long id, CartProductStatus cartProductStatus);


}
