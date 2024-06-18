package com.example.gamewebshop.model;

import com.example.gamewebshop.models.Category;
import com.example.gamewebshop.models.PromoCode;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PromoCodeModelTest {

    @Test
    public void should_return_correct_values_when_getter_and_setter_are_called() {
        // Arrange
        PromoCode promoCode = new PromoCode();
        LocalDateTime now = LocalDateTime.now();

        // Act
        promoCode.setId(1L);
        promoCode.setCode("TESTCODE");
        promoCode.setDiscount(20.0);
        promoCode.setExpiryDate(now);
        promoCode.setStartDate(now.minusDays(1));
        promoCode.setMaxUsageCount(100);
        promoCode.setType(PromoCode.PromoCodeType.PERCENTAGE);
        promoCode.setMinSpendAmount(50.0);
        promoCode.setUsageCount(10);
        promoCode.setTotalDiscountAmount(200.0);

        // Assert
        assertEquals(1L, promoCode.getId());
        assertEquals("TESTCODE", promoCode.getCode());
        assertEquals(20.0, promoCode.getDiscount());
        assertEquals(now, promoCode.getExpiryDate());
        assertEquals(now.minusDays(1), promoCode.getStartDate());
        assertEquals(100, promoCode.getMaxUsageCount());
        assertEquals(PromoCode.PromoCodeType.PERCENTAGE, promoCode.getType());
        assertEquals(50.0, promoCode.getMinSpendAmount());
        assertEquals(10, promoCode.getUsageCount());
        assertEquals(200.0, promoCode.getTotalDiscountAmount());
    }

    @Test
    public void should_initialize_correctly_when_constructor_is_called() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Category category = new Category();
        PromoCode promoCode = new PromoCode("TESTCODE", 20.0, now, now.minusDays(1), 100, PromoCode.PromoCodeType.PERCENTAGE, category, 50.0);

        // Assert
        assertEquals("TESTCODE", promoCode.getCode());
        assertEquals(20.0, promoCode.getDiscount());
        assertEquals(now, promoCode.getExpiryDate());
        assertEquals(now.minusDays(1), promoCode.getStartDate());
        assertEquals(100, promoCode.getMaxUsageCount());
        assertEquals(PromoCode.PromoCodeType.PERCENTAGE, promoCode.getType());
        assertEquals(category, promoCode.getCategory());
        assertEquals(50.0, promoCode.getMinSpendAmount());
        assertEquals(0, promoCode.getUsageCount());
        assertEquals(0.0, promoCode.getTotalDiscountAmount());
    }

}
