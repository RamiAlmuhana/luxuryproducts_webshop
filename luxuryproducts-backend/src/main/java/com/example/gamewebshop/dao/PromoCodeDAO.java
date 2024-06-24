package com.example.gamewebshop.dao;

import com.example.gamewebshop.Repositorys.CategoryRepository;
import com.example.gamewebshop.Repositorys.PromoCodeRepository;
import com.example.gamewebshop.models.Category;
import com.example.gamewebshop.models.PromoCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
public class PromoCodeDAO {
    private final PromoCodeRepository promoCodeRepository;
    private final CategoryRepository categoryRepository;



    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepository.findAll();
    }

    public PromoCode addPromoCode(PromoCode promoCode) {
        if (promoCode.getCategory() != null) {
            Category category = categoryRepository.findById(promoCode.getCategory().getId()).orElse(null);
            promoCode.setCategory(category);
        }
        return promoCodeRepository.save(promoCode);
    }

    public PromoCode updatePromoCode(Long id, PromoCode promoCodeDetails) {
        Optional<PromoCode> promoCodeOptional = promoCodeRepository.findById(id);
        if (promoCodeOptional.isPresent()) {
            PromoCode existingPromoCode = promoCodeOptional.get();
            existingPromoCode.setCode(promoCodeDetails.getCode());
            existingPromoCode.setDiscount(promoCodeDetails.getDiscount());
            existingPromoCode.setExpiryDate(promoCodeDetails.getExpiryDate());
            existingPromoCode.setStartDate(promoCodeDetails.getStartDate());
            existingPromoCode.setMaxUsageCount(promoCodeDetails.getMaxUsageCount());
            existingPromoCode.setMinSpendAmount(promoCodeDetails.getMinSpendAmount());
            if (promoCodeDetails.getCategory() != null) {
                Category category = categoryRepository.findById(promoCodeDetails.getCategory().getId()).orElse(null);
                existingPromoCode.setCategory(category);
            } else {
                existingPromoCode.setCategory(null);
            }
            return promoCodeRepository.save(existingPromoCode);
        } else {
            return null;
        }
    }

    public void deletePromoCode(Long id) {
        promoCodeRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return promoCodeRepository.existsById(id);
    }

    public Optional<PromoCode> getPromoCodeByCode(String code) {
        return promoCodeRepository.findByCode(code);
    }

    public boolean isPromoCodeValid(String code) {
        Optional<PromoCode> promoCodeOptional = getPromoCodeByCode(code);
        return promoCodeOptional.isPresent() && promoCodeOptional.get().getExpiryDate().isAfter(LocalDateTime.now()) && promoCodeOptional.get().getMaxUsageCount() > 0 && promoCodeOptional.get().getStartDate().isBefore(LocalDateTime.now());
    }

    public Optional<PromoCode> getPromoCodeById(Long id) {
        return promoCodeRepository.findById(id);
    }

    public Optional<PromoCode> getPromoCodeByCategory(long categoryId) {
        return promoCodeRepository.findFirstByCategoryId(categoryId);
    }

    public Optional<List<PromoCode>> getAllPromoCodeByCategory(long categoryId) {
        return promoCodeRepository.findAllByCategoryId(categoryId);
    }

}
