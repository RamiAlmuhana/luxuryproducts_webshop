package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.OrderDAO;
import com.example.gamewebshop.Repositorys.UserRepository;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderUserDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.PlacedOrder;
import lombok.AllArgsConstructor;
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

    // todo : fix order plaatsen
//    @PostMapping
//    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody PlacedOrder placedOrder, Principal principal) {
//        String userEmail = principal.getName();
//        CustomUser user = userRepository.findByEmail(userEmail);
//        if (user == null) {
//            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
//        }
//        try {
//            String promoCode = placedOrder.getPromoCode();
//            String giftCardCode = placedOrder.getGiftCardCode();
//
//
//            orderDAO.createOrder(placedOrder, userEmail, promoCode, giftCardCode);
//            return ResponseEntity.ok(Map.of(
//                    "message", "Order created successfully",
//                    "totalPrice", placedOrder.getTotalPrice(),
//                    "discountedPrice", placedOrder.getDiscountedPrice(),
//                    "promoCode", promoCode != null ? promoCode : "Automatic discount applied",
//                    "discount", placedOrder.getTotalPrice() - placedOrder.getDiscountedPrice(),
//                    "giftCardCode", giftCardCode != null ? giftCardCode : "No gift card applied"
//            ));
//        } catch (ResponseStatusException e) {
//            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()));
//        }
//    }


}
