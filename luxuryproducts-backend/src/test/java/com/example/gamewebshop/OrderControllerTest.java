//package com.example.gamewebshop;
//
//import com.example.gamewebshop.controller.OrderController;
//import com.example.gamewebshop.dao.OrderDAO;
//import com.example.gamewebshop.Repositorys.UserRepository;
//import com.example.gamewebshop.models.CustomUser;
//import com.example.gamewebshop.models.PlacedOrder;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.security.Principal;
//import java.util.Map;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.notNullValue;
//import static org.mockito.Mockito.*;
//
//public class OrderControllerTest {
//
//    @Mock
//    private OrderDAO orderDAO;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private OrderController orderController;
//
//    @Mock
//    private Principal principal;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void should_create_order_successfully() {
//        // Arrange
//        CustomUser user = new CustomUser();
//        user.setEmail("test@example.com");
//        when(principal.getName()).thenReturn("test@example.com");
//        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
//
//        PlacedOrder placedOrder = new PlacedOrder();
//
//        // Act
//        ResponseEntity<Map<String, Object>> response = orderController.createOrder(placedOrder, principal, null);
//
//        // Assert
//        assertThat(response.getStatusCodeValue(), is(200));
//        Map<String, Object> responseBody = response.getBody();
//        assertThat(responseBody, is(notNullValue()));
//        assertThat(responseBody.get("message"), is("Order created successfully"));
//
//        // Verify
//        verify(orderDAO, times(1)).createOrder(placedOrder, "test@example.com", null);
//    }
//
//    @Test
//    public void should_return_bad_request_when_user_not_found() {
//        // Arrange
//        when(principal.getName()).thenReturn("test@example.com");
//        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
//
//        PlacedOrder placedOrder = new PlacedOrder();
//
//        // Act
//        ResponseEntity<Map<String, Object>> response = orderController.createOrder(placedOrder, principal, null);
//
//        // Assert
//        assertThat(response.getStatusCodeValue(), is(400));
//        Map<String, Object> responseBody = response.getBody();
//        assertThat(responseBody, is(notNullValue()));
//        assertThat(responseBody.get("message"), is("User not found"));
//
//        // Verify
//        verify(orderDAO, never()).createOrder(any(PlacedOrder.class), anyString(), anyString());
//    }
//
//    @Test
//    public void should_return_bad_request_when_invalid_promo_code() {
//        // Arrange
//        CustomUser user = new CustomUser();
//        user.setEmail("test@example.com");
//        when(principal.getName()).thenReturn("test@example.com");
//        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
//
//        PlacedOrder placedOrder = new PlacedOrder();
//
//        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired promo code"))
//                .when(orderDAO).createOrder(placedOrder, "test@example.com", "INVALIDCODE");
//
//        // Act
//        ResponseEntity<Map<String, Object>> response = orderController.createOrder(placedOrder, principal, "INVALIDCODE");
//
//        // Assert
//        assertThat(response.getStatusCodeValue(), is(400));
//        Map<String, Object> responseBody = response.getBody();
//        assertThat(responseBody, is(notNullValue()));
//        assertThat(responseBody.get("message"), is("Invalid or expired promo code"));
//
//        // Verify
//        verify(orderDAO, times(1)).createOrder(placedOrder, "test@example.com", "INVALIDCODE");
//    }
//
//    @Test
//    public void should_return_bad_request_when_total_price_does_not_meet_minimum_spend() {
//        // Arrange
//        CustomUser user = new CustomUser();
//        user.setEmail("test@example.com");
//        when(principal.getName()).thenReturn("test@example.com");
//        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
//
//        PlacedOrder placedOrder = new PlacedOrder();
//
//        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Total price does not meet the minimum spend amount for this promo code"))
//                .when(orderDAO).createOrder(placedOrder, "test@example.com", "PROMOCODE");
//
//        // Act
//        ResponseEntity<Map<String, Object>> response = orderController.createOrder(placedOrder, principal, "PROMOCODE");
//
//        // Assert
//        assertThat(response.getStatusCodeValue(), is(400));
//        Map<String, Object> responseBody = response.getBody();
//        assertThat(responseBody, is(notNullValue()));
//        assertThat(responseBody.get("message"), is("Total price does not meet the minimum spend amount for this promo code"));
//
//        // Verify
//        verify(orderDAO, times(1)).createOrder(placedOrder, "test@example.com", "PROMOCODE");
//    }
//}
