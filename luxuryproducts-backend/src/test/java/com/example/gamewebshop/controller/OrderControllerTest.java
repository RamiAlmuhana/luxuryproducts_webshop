package com.example.gamewebshop.controller;

import com.example.gamewebshop.controller.OrderController;
import com.example.gamewebshop.dao.OrderDAO;
import com.example.gamewebshop.Repositorys.UserRepository;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderDTO;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderUserDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.PlacedOrder;
import com.example.gamewebshop.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderController orderController;

    @Mock
    private Principal principal;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_return_all_orders() {
        // Arrange
        List<PlacedOrder> orders = new ArrayList<>();
        when(orderDAO.getAllOrders()).thenReturn(orders);

        // Act
        ResponseEntity<List<PlacedOrder>> response = orderController.getAllOrders();

        // Assert
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(orders));

        // Verify
        verify(orderDAO, times(1)).getAllOrders();
    }

    @Test
    public void should_return_orders_by_user_principal() {
        // Arrange
        CustomUser user = new CustomUser();
        when(principal.getName()).thenReturn("user@example.com");
        when(userService.validateUser(principal)).thenReturn(user);
        List<OrderUserDTO> orders = new ArrayList<>();
        when(orderDAO.getOrdersByUserId(user)).thenReturn(orders);

        // Act
        ResponseEntity<List<OrderUserDTO>> response = orderController.getOrdersByUserPrincipal(principal);

        // Assert
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(orders));

        // Verify
        verify(userService, times(1)).validateUser(principal);
        verify(orderDAO, times(1)).getOrdersByUserId(user);
    }

    @Test
    public void should_return_404_when_user_not_found_by_id() {
        // Arrange
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<List<OrderUserDTO>> response = orderController.getOrdersByUserId(userId);

        // Assert
        assertThat(response.getStatusCodeValue(), is(404));
        assertThat(response.getBody(), is(nullValue()));

        // Verify
        verify(userRepository, times(1)).findById(userId);
        verify(orderDAO, never()).getOrdersByUserIdForDashboard(any());
    }

    @Test
    public void should_return_orders_when_user_found_by_id() {
        // Arrange
        long userId = 1L;
        CustomUser user = new CustomUser();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        List<OrderUserDTO> orders = new ArrayList<>();
        when(orderDAO.getOrdersByUserIdForDashboard(user)).thenReturn(orders);

        // Act
        ResponseEntity<List<OrderUserDTO>> response = orderController.getOrdersByUserId(userId);

        // Assert
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(orders));

        // Verify
        verify(userRepository, times(1)).findById(userId);
        verify(orderDAO, times(1)).getOrdersByUserIdForDashboard(user);
    }

    @Test
    public void should_create_order_successfully() {
        // Arrange
        CustomUser user = new CustomUser();
        user.setEmail("test@example.com");
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        OrderDTO orderDTO = new OrderDTO();

        // Act
        ResponseEntity<String> response = orderController.createOrder(orderDTO, principal);

        // Assert
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is("{\"message\": \"Order created successfully\"}"));

        // Verify
        verify(orderDAO, times(1)).saveOrderWithProducts(orderDTO, "test@example.com");
    }

    @Test
    public void should_throw_exception_when_order_creation_fails() {

        // Arrange
        CustomUser user = new CustomUser();
        user.setEmail("test@example.com");
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        OrderDTO orderDTO = new OrderDTO();

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "order couldn't be created"))
                .when(orderDAO).saveOrderWithProducts(orderDTO, "test@example.com");

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderController.createOrder(orderDTO, principal);
        });

        assertThat(exception.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(exception.getReason(), is("order couldn't be created org.springframework.web.server.ResponseStatusException: 404 NOT_FOUND \"order couldn't be created\""));

        // Verify
        verify(orderDAO, times(1)).saveOrderWithProducts(orderDTO, "test@example.com");

    }
}
