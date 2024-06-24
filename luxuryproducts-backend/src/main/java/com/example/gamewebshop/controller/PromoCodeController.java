package com.example.gamewebshop.controller;

import com.example.gamewebshop.Repositorys.PromoCodeRepository;
import com.example.gamewebshop.models.PromoCode;
import com.example.gamewebshop.dao.PromoCodeDAO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequestMapping("/promocodes")
public class PromoCodeController {

    private final PromoCodeDAO promoCodeDAO;
    private final PromoCodeRepository promoCodeRepository;

    @GetMapping
    public ResponseEntity<List<PromoCode>> getAllPromoCodes() {
        List<PromoCode> promoCodes = promoCodeDAO.getAllPromoCodes();
        return ResponseEntity.ok(promoCodes);
    }

    @PostMapping
    public ResponseEntity<PromoCode> addPromoCode(@RequestBody PromoCode promoCode) {
        PromoCode newPromoCode = promoCodeDAO.addPromoCode(promoCode);
        return ResponseEntity.ok(newPromoCode);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromoCode> updatePromoCode(@PathVariable Long id, @RequestBody PromoCode promoCodeDetails) {
        PromoCode updatedPromoCode = promoCodeDAO.updatePromoCode(id, promoCodeDetails);
        return ResponseEntity.ok(updatedPromoCode);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePromoCode(@PathVariable Long id) {
        if (!promoCodeDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        promoCodeDAO.deletePromoCode(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromoCode> getPromoCodeById(@PathVariable Long id) {
        Optional<PromoCode> promoCode = promoCodeDAO.getPromoCodeById(id);
        return promoCode.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validatePromoCode(@RequestParam String code) {
        Optional<PromoCode> promoCode = promoCodeDAO.getPromoCodeByCode(code);
        if (promoCode.isPresent() && promoCodeDAO.isPromoCodeValid(code)) {
            PromoCode validPromoCode = promoCode.get();
            Long categoryId = validPromoCode.getCategory() != null ? validPromoCode.getCategory().getId() : null;

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("discount", validPromoCode.getDiscount());
            responseMap.put("type", validPromoCode.getType().toString());
            responseMap.put("minSpendAmount", validPromoCode.getMinSpendAmount());
            responseMap.put("startDate", validPromoCode.getStartDate());
            responseMap.put("expiryDate", validPromoCode.getExpiryDate());
            responseMap.put("categoryId", categoryId);

            return ResponseEntity.ok(responseMap);
        }
        return ResponseEntity.badRequest().build();
    }


    @GetMapping("/promocode-stats")
    public ResponseEntity<List<Map<String, Object>>> getPromoCodeStats() {
        List<PromoCode> promoCodes = promoCodeRepository.findAll();
        List<Map<String, Object>> promoCodeStats = promoCodes.stream()
                .map(promoCode -> Map.<String, Object>of(
                        "code", promoCode.getCode(),
                        "usageCount", promoCode.getUsageCount(),
                        "totalDiscountAmount", promoCode.getTotalDiscountAmount(),
                        "discount", promoCode.getDiscount(),
                        "expiryDate", promoCode.getExpiryDate(),
                        "startDate", promoCode.getStartDate(),
                        "maxUsageCount", promoCode.getMaxUsageCount(),
                        "type", promoCode.getType(),
                        "minSpendAmount", promoCode.getMinSpendAmount()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(promoCodeStats);
    }

}
