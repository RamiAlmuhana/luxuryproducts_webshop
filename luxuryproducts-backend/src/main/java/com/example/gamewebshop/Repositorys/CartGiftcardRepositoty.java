package com.example.gamewebshop.Repositorys;

import com.example.gamewebshop.models.CartGiftcard;
import com.example.gamewebshop.models.Product.Enums.CartProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartGiftcardRepositoty  extends JpaRepository<CartGiftcard, Long> {

    Optional<List<CartGiftcard>> getCartGiftcardsByUserIdAndStatus(long id, CartProductStatus cartProductStatus);


}
