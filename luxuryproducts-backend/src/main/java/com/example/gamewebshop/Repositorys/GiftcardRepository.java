package com.example.gamewebshop.Repositorys;

import com.example.gamewebshop.models.Giftcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiftcardRepository extends JpaRepository<Giftcard, Long> {
    Optional<Giftcard> findByCode(String code);
}
