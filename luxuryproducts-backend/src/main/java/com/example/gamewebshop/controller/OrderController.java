package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.OrderDAO;
import com.example.gamewebshop.Repositorys.UserRepository;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderDTO;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderUserDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.PlacedOrder;
import com.example.gamewebshop.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequestMapping("/orders")
public class OrderController {

    private final OrderDAO orderDAO;
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<PlacedOrder>> getAllOrders() {
        return ResponseEntity.ok(this.orderDAO.getAllOrders());
    }

    @GetMapping("/myOrders")
    public ResponseEntity<List<OrderUserDTO>> getOrdersByUserPrincipal(Principal principal) {
        CustomUser user = userService.validateUser(principal);
        List<OrderUserDTO> orders = this.orderDAO.getOrdersByUserId(user);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/myOrders/{id}")
    public ResponseEntity<List<OrderUserDTO>> getOrdersByUserId(@PathVariable long id) {
        Optional<CustomUser> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<OrderUserDTO> orders = this.orderDAO.getOrdersByUserIdForDashboard(user.get());
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO, Principal principal) {
        String userEmail = principal.getName();
        try {
            orderDAO.saveOrderWithProducts(orderDTO, userEmail);
            return ResponseEntity.ok().body("{\"message\": \"Order created successfully\"}");
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "order couldn't be created " + e
            );
        }
    }
}
