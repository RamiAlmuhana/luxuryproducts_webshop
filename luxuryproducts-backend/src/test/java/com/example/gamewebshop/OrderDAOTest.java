//package com.example.gamewebshop;
//
//import com.example.gamewebshop.dao.OrderDAO;
//import com.example.gamewebshop.Repositorys.OrderRepository;
//import com.example.gamewebshop.Repositorys.ProductRepository;
//import com.example.gamewebshop.Repositorys.UserRepository;
//import com.example.gamewebshop.models.CustomUser;
//import com.example.gamewebshop.models.PlacedOrder;
//import com.example.gamewebshop.models.Product.Product;
//import com.example.gamewebshop.models.PromoCode;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.HashSet;
//import java.util.Optional;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.is;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//public class OrderDAOTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    private OrderDAO orderDAO;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void should_calculate_total_price_correctly() {
//        // Arrange
//        Product product1 = new Product();
//        product1.setPrice(50.0);
//        Product product2 = new Product();
//        product2.setPrice(70.0);
//
//        PlacedOrder placedOrder = new PlacedOrder();
//        placedOrder.setProducts(new HashSet<>(java.util.Arrays.asList(product1, product2)));
//
//        // Act
//        double totalPrice = orderDAO.calculateTotalPrice(placedOrder);
//
//        // Assert
//        assertThat(totalPrice, is(120.0));
//    }
//
//    @Test
//    public void should_calculate_discount_correctly_for_fixed_amount() {
//        // Arrange
//        PromoCode promoCode = new PromoCode();
//        promoCode.setType(PromoCode.PromoCodeType.FIXED_AMOUNT);
//        promoCode.setDiscount(30.0);
//
//        // Act
//        double discount = orderDAO.calculateDiscount(100.0, promoCode);
//
//        // Assert
//        assertThat(discount, is(30.0));
//    }
//
//    @Test
//    public void should_calculate_discount_correctly_for_percentage() {
//        // Arrange
//        PromoCode promoCode = new PromoCode();
//        promoCode.setType(PromoCode.PromoCodeType.PERCENTAGE);
//        promoCode.setDiscount(10.0);
//
//        // Act
//        double discount = orderDAO.calculateDiscount(100.0, promoCode);
//
//        // Assert
//        assertThat(discount, is(10.0));
//    }
//
//    @Test
//    public void should_throw_exception_when_user_not_found() {
//        // Arrange
//        PlacedOrder placedOrder = new PlacedOrder();
//        String userEmail = "nonexistent@example.com";
//
//        when(userRepository.findByEmail(userEmail)).thenReturn(null);
//
//        // Act & Assert
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
//            orderDAO.createOrder(placedOrder, userEmail, null);
//        });
//
//        assertThat(exception.getReason(), is("User not found"));
//    }
//
//    @Test
//    public void should_throw_exception_when_product_not_found() {
//        // Arrange
//        CustomUser user = new CustomUser();
//        user.setEmail("test@example.com");
//        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
//
//        Product product1 = new Product();
//        product1.setId(1L);
//        product1.setPrice(50.0);
//
//        PlacedOrder placedOrder = new PlacedOrder();
//        placedOrder.setProducts(new HashSet<>(java.util.Collections.singletonList(product1)));
//
//        when(productRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
//            orderDAO.createOrder(placedOrder, "test@example.com", null);
//        });
//
//        assertThat(exception.getReason(), is("Product not found"));
//    }
//
//    @Test
//    public void should_throw_exception_when_no_orders_found_for_user() {
//        // Arrange
//        long userId = 1L;
//        when(orderRepository.findByUserId(userId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
//            orderDAO.getOrdersByUserId(userId);
//        });
//
//        assertThat(exception.getReason(), is("No orders found for this user"));
//    }
//}
