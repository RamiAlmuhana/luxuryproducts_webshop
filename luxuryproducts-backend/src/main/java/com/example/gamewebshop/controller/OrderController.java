package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.OrderDAO;
import com.example.gamewebshop.Repositorys.UserRepository;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderDTO;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderUserDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.PlacedOrder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/orders")
public class OrderController {

    private final OrderDAO orderDAO;
    private final UserRepository userRepository;


    @GetMapping
    public ResponseEntity<List<PlacedOrder>> getAllOrders(){
        return ResponseEntity.ok(this.orderDAO.getAllOrders());
    }


    @GetMapping("/myOrders")
    public ResponseEntity<List<OrderUserDTO>> getOrdersByUserPrincipal(Principal principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().build();
        }
        String userEmail = principal.getName();
        CustomUser user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<OrderUserDTO> orders = this.orderDAO.getOrdersByUserId(user);

        return ResponseEntity.ok(orders);
    }

//    @PostMapping
//    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO, Principal principal) {
//        String userEmail = principal.getName();
//        this.orderDAO.saveOrderWithProducts(orderDTO, userEmail);
//        return ResponseEntity.ok().body("{\"message\": \"Order created successfully\"}");
//    }

    // todo : fix order plaatsen
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO, Principal principal) {
        String userEmail = principal.getName();

        try {
            orderDAO.saveOrderWithProducts(orderDTO, userEmail);
            return ResponseEntity.ok().body("{\"message\": \"Order created successfully\"}");
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "order couldnt be created " + e
            );
        }
    }


}
