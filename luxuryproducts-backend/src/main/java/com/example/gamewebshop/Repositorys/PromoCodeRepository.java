package com.example.gamewebshop.Repositorys;

import com.example.gamewebshop.models.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
    Optional<PromoCode> findByCode(String code);
    Optional<PromoCode> findFirstByCategoryId(long categoryId);
}
