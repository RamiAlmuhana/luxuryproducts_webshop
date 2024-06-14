package com.example.gamewebshop.Repositorys;


import com.example.gamewebshop.models.Product.CartProduct;
import com.example.gamewebshop.models.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReturnRepository extends JpaRepository<ReturnRequest, Long> {
    Optional<List<ReturnRequest>> findByUserId(long id);
    Optional<ReturnRequest> findByCartProduct(CartProduct cartProduct);
}
