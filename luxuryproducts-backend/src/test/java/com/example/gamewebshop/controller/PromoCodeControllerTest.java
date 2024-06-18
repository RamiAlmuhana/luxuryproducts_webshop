package com.example.gamewebshop.controller;

import com.example.gamewebshop.controller.PromoCodeController;
import com.example.gamewebshop.Repositorys.PromoCodeRepository;
import com.example.gamewebshop.models.PromoCode;
import com.example.gamewebshop.models.PromoCode.PromoCodeType;
import com.example.gamewebshop.dao.PromoCodeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.eq;

public class PromoCodeControllerTest {

    @Mock
    private PromoCodeDAO promoCodeDAO;

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @InjectMocks
    private PromoCodeController promoCodeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_return_all_promo_codes_when_getAllPromoCodes_is_called() {
        // Arrange
        PromoCode promoCode1 = new PromoCode();
        promoCode1.setCode("PROMO1");
        PromoCode promoCode2 = new PromoCode();
        promoCode2.setCode("PROMO2");

        List<PromoCode> promoCodes = Arrays.asList(promoCode1, promoCode2);
        when(promoCodeDAO.getAllPromoCodes()).thenReturn(promoCodes);

        // Act
        ResponseEntity<List<PromoCode>> response = promoCodeController.getAllPromoCodes();

        // Assert
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), hasSize(2));
        assertThat(response.getBody(), contains(promoCode1, promoCode2));

        // Verify
        verify(promoCodeDAO, times(1)).getAllPromoCodes();
        verifyNoMoreInteractions(promoCodeDAO);
    }

    @Test
    public void should_return_new_promo_code_when_addPromoCode_is_called() {
        // Arrange
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("PROMO1");

        when(promoCodeDAO.addPromoCode(promoCode)).thenReturn(promoCode);

        // Act
        ResponseEntity<PromoCode> response = promoCodeController.addPromoCode(promoCode);

        // Assert
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getCode(), is("PROMO1"));

        // Verify
        verify(promoCodeDAO, times(1)).addPromoCode(promoCode);
        verifyNoMoreInteractions(promoCodeDAO);
    }

    @Test
    public void should_return_updated_promo_code_when_updatePromoCode_is_called() {
        // Arrange
        Long id = 1L;
        PromoCode existingPromoCode = new PromoCode();
        existingPromoCode.setId(id);
        existingPromoCode.setCode("OLD_CODE");

        PromoCode updatedDetails = new PromoCode();
        updatedDetails.setCode("NEW_CODE");

        PromoCode updatedPromoCode = new PromoCode();
        updatedPromoCode.setId(id);
        updatedPromoCode.setCode("NEW_CODE");

        when(promoCodeDAO.updatePromoCode(id, updatedDetails)).thenReturn(updatedPromoCode);

        // Act
        ResponseEntity<PromoCode> response = promoCodeController.updatePromoCode(id, updatedDetails);

        // Assert
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getCode(), is("NEW_CODE"));

        // Verify
        verify(promoCodeDAO, times(1)).updatePromoCode(id, updatedDetails);
        verifyNoMoreInteractions(promoCodeDAO);
    }

    @Test
    public void should_delete_promo_code_when_deletePromoCode_is_called() {
        // Arrange
        Long id = 1L;
        when(promoCodeDAO.existsById(id)).thenReturn(true);

        // Act
        ResponseEntity<?> response = promoCodeController.deletePromoCode(id);

        // Assert
        assertThat(response.getStatusCodeValue(), is(200));

        // Verify
        verify(promoCodeDAO, times(1)).deletePromoCode(id);
        verify(promoCodeDAO, times(1)).existsById(id);
        verifyNoMoreInteractions(promoCodeDAO);
    }

    @Test
    public void should_return_not_found_when_deletePromoCode_is_called_with_non_existing_id() {
        // Arrange
        Long id = 1L;
        when(promoCodeDAO.existsById(id)).thenReturn(false);

        // Act
        ResponseEntity<?> response = promoCodeController.deletePromoCode(id);

        // Assert
        assertThat(response.getStatusCodeValue(), is(404));

        // Verify
        verify(promoCodeDAO, times(1)).existsById(id);
        verifyNoMoreInteractions(promoCodeDAO);
    }

    @Test
    public void should_return_promo_code_when_getPromoCodeById_is_called() {
        // Arrange
        Long id = 1L;
        PromoCode promoCode = new PromoCode();
        promoCode.setId(id);
        promoCode.setCode("PROMO1");

        when(promoCodeDAO.getPromoCodeById(id)).thenReturn(Optional.of(promoCode));

        // Act
        ResponseEntity<PromoCode> response = promoCodeController.getPromoCodeById(id);

        // Assert
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getCode(), is("PROMO1"));

        // Verify
        verify(promoCodeDAO, times(1)).getPromoCodeById(id);
        verifyNoMoreInteractions(promoCodeDAO);
    }

    @Test
    public void should_return_not_found_when_getPromoCodeById_is_called_with_non_existing_id() {
        // Arrange
        Long id = 1L;
        when(promoCodeDAO.getPromoCodeById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<PromoCode> response = promoCodeController.getPromoCodeById(id);

        // Assert
        assertThat(response.getStatusCodeValue(), is(404));

        // Verify
        verify(promoCodeDAO, times(1)).getPromoCodeById(id);
        verifyNoMoreInteractions(promoCodeDAO);
    }

    @Test
    public void should_return_validated_promo_code_when_validatePromoCode_is_called_with_valid_code() {
        // Arrange
        String code = "VALIDCODE";
        PromoCode promoCode = new PromoCode();
        promoCode.setCode(code);
        promoCode.setDiscount(20.0);
        promoCode.setType(PromoCodeType.PERCENTAGE);
        promoCode.setMinSpendAmount(100.0);
        promoCode.setStartDate(LocalDateTime.now().minusDays(1));
        promoCode.setExpiryDate(LocalDateTime.now().plusDays(1));

        when(promoCodeDAO.getPromoCodeByCode(code)).thenReturn(Optional.of(promoCode));
        when(promoCodeDAO.isPromoCodeValid(code)).thenReturn(true);

        // Act
        ResponseEntity<?> response = promoCodeController.validatePromoCode(code);

        // Assert
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(instanceOf(Map.class)));

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body.get("discount"), is(20.0));
        assertThat(body.get("type"), is("PERCENTAGE"));
        assertThat(body.get("minSpendAmount"), is(100.0));
        assertThat(body.get("startDate"), is(notNullValue()));
        assertThat(body.get("expiryDate"), is(notNullValue()));

        // Verify
        verify(promoCodeDAO, times(1)).getPromoCodeByCode(code);
        verify(promoCodeDAO, times(1)).isPromoCodeValid(code);
        verifyNoMoreInteractions(promoCodeDAO);
    }

    @Test
    public void should_return_bad_request_when_validatePromoCode_is_called_with_invalid_code() {
        // Arrange
        String code = "INVALIDCODE";

        when(promoCodeDAO.getPromoCodeByCode(code)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = promoCodeController.validatePromoCode(code);

        // Assert
        assertThat(response.getStatusCodeValue(), is(400));

        // Verify
        verify(promoCodeDAO, times(1)).getPromoCodeByCode(code);
        verifyNoMoreInteractions(promoCodeDAO);
    }

    @Test
    public void should_return_promo_code_stats_when_getPromoCodeStats_is_called() {
        // Arrange
        PromoCode promoCode1 = new PromoCode();
        promoCode1.setCode("PROMO1");
        promoCode1.setUsageCount(10);
        promoCode1.setTotalDiscountAmount(100.0);
        promoCode1.setDiscount(10);
        promoCode1.setExpiryDate(LocalDateTime.now().plusDays(10));
        promoCode1.setStartDate(LocalDateTime.now().minusDays(10));
        promoCode1.setMaxUsageCount(100);
        promoCode1.setType(PromoCodeType.PERCENTAGE);
        promoCode1.setMinSpendAmount(50.0);

        PromoCode promoCode2 = new PromoCode();
        promoCode2.setCode("PROMO2");
        promoCode2.setUsageCount(20);
        promoCode2.setTotalDiscountAmount(200.0);
        promoCode2.setDiscount(20);
        promoCode2.setExpiryDate(LocalDateTime.now().plusDays(20));
        promoCode2.setStartDate(LocalDateTime.now().minusDays(20));
        promoCode2.setMaxUsageCount(200);
        promoCode2.setType(PromoCodeType.FIXED_AMOUNT);
        promoCode2.setMinSpendAmount(100.0);

        List<PromoCode> promoCodes = Arrays.asList(promoCode1, promoCode2);
        when(promoCodeRepository.findAll()).thenReturn(promoCodes);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = promoCodeController.getPromoCodeStats();

        // Assert
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), hasSize(2));

        Map<String, Object> promoCodeStats1 = response.getBody().get(0);
        assertThat(promoCodeStats1.get("code"), is("PROMO1"));
        assertThat(promoCodeStats1.get("usageCount"), is(10));
        assertThat(promoCodeStats1.get("totalDiscountAmount"), is(100.0));
        assertThat(promoCodeStats1.get("discount"), is(10.0));
        assertThat(promoCodeStats1.get("expiryDate"), is(notNullValue()));
        assertThat(promoCodeStats1.get("startDate"), is(notNullValue()));
        assertThat(promoCodeStats1.get("maxUsageCount"), is(100));
        assertThat(promoCodeStats1.get("type"), is(PromoCodeType.PERCENTAGE));
        assertThat(promoCodeStats1.get("minSpendAmount"), is(50.0));

        Map<String, Object> promoCodeStats2 = response.getBody().get(1);
        assertThat(promoCodeStats2.get("code"), is("PROMO2"));
        assertThat(promoCodeStats2.get("usageCount"), is(20));
        assertThat(promoCodeStats2.get("totalDiscountAmount"), is(200.0));
        assertThat(promoCodeStats2.get("discount"), is(20.0));
        assertThat(promoCodeStats2.get("expiryDate"), is(notNullValue()));
        assertThat(promoCodeStats2.get("startDate"), is(notNullValue()));
        assertThat(promoCodeStats2.get("maxUsageCount"), is(200));
        assertThat(promoCodeStats2.get("type"), is(PromoCodeType.FIXED_AMOUNT));
        assertThat(promoCodeStats2.get("minSpendAmount"), is(100.0));

        // Verify
        verify(promoCodeRepository, times(1)).findAll();
        verifyNoMoreInteractions(promoCodeRepository);
    }
}
