package com.example.gamewebshop.dao;

import com.example.gamewebshop.dao.GiftcardDAO;
import com.example.gamewebshop.dao.OrderDAO;
import com.example.gamewebshop.Repositorys.*;
import com.example.gamewebshop.dao.ProductDAO;
import com.example.gamewebshop.dao.PromoCodeDAO;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderDTO;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderUserDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Giftcard;
import com.example.gamewebshop.models.PlacedOrder;
import com.example.gamewebshop.models.Product.CartProduct;
import com.example.gamewebshop.models.Product.Product;
import com.example.gamewebshop.models.PromoCode;
import com.example.gamewebshop.services.CartGiftcardService;
import com.example.gamewebshop.services.CartProductService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderDAOTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReturnRepository returnRepository;

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @Mock
    private PromoCodeDAO promoCodeDAO;

    @Mock
    private GiftcardRepository giftcardRepository;

    @Mock
    private CartProductRepository cartProductRepository;

    @Mock
    private CartGiftcardService cartGiftcardService;

    @Mock
    private GiftcardDAO giftcardDAO;

    @Mock
    private CartProductService cartProductService;

    @Mock
    private ProductDAO productDAO;

    @InjectMocks
    private OrderDAO orderDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_calculate_discount_correctly_for_fixed_amount() {
        // Arrange
        PromoCode promoCode = new PromoCode();
        promoCode.setType(PromoCode.PromoCodeType.FIXED_AMOUNT);
        promoCode.setDiscount(30.0);

        // Act
        double discount = orderDAO.calculateDiscount(100.0, promoCode);

        // Assert
        assertThat(discount, is(30.0));
    }

    @Test
    public void should_calculate_discount_correctly_for_percentage() {
        // Arrange
        PromoCode promoCode = new PromoCode();
        promoCode.setType(PromoCode.PromoCodeType.PERCENTAGE);
        promoCode.setDiscount(10.0);

        // Act
        double discount = orderDAO.calculateDiscount(100.0, promoCode);

        // Assert
        assertThat(discount, is(10.0));
    }

    @Test
    public void should_throw_exception_when_user_not_found() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        String userEmail = "nonexistent@example.com";

        when(userRepository.findByEmail(userEmail)).thenReturn(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderDAO.saveOrderWithProducts(orderDTO, userEmail);
        });

        assertThat(exception.getReason(), is("User not found"));
    }

    @Test
    public void should_throw_exception_when_no_orders_found_for_user() {
        // Arrange
        CustomUser user = new CustomUser();
        when(orderRepository.findAllByUser(user)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderDAO.getOrdersByUserId(user);
        });

        assertThat(exception.getReason(), is("No orders found with that user"));
    }

    @Test
    public void should_validate_and_throw_exception_when_invalid_promo_code() {
        // Arrange
        String promoCode = "INVALID_CODE";
        double totalPrice = 100.0;

        when(promoCodeDAO.getPromoCodeByCode(promoCode)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderDAO.validatePromocode(promoCode, totalPrice);
        });

        assertThat(exception.getReason(), is("Invalid or expired promo code"));
    }

    @Test
    public void should_validate_and_throw_exception_when_total_price_below_minimum_spend() {
        // Arrange
        PromoCode promoCode = new PromoCode();
        promoCode.setType(PromoCode.PromoCodeType.FIXED_AMOUNT);
        promoCode.setDiscount(30.0);
        promoCode.setMinSpendAmount(150.0);

        when(promoCodeDAO.getPromoCodeByCode("VALID_CODE")).thenReturn(Optional.of(promoCode));
        when(promoCodeDAO.isPromoCodeValid("VALID_CODE")).thenReturn(true);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderDAO.validatePromocode("VALID_CODE", 100.0);
        });

        assertThat(exception.getReason(), is("Total price does not meet the minimum spend amount for this promo code"));
    }

}
