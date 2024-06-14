package com.example.gamewebshop.controller;

import com.example.gamewebshop.dto.CartGiftCardDTO;
import com.example.gamewebshop.models.CartGiftcard;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Giftcard;
import com.example.gamewebshop.services.AdminService;
import com.example.gamewebshop.services.CartGiftcardService;
import com.example.gamewebshop.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/cartGiftCard")
public class CartGiftcardController {

    private final CartGiftcardService cartGiftcardService;
    private final UserService userService;


    @PostMapping
    public ResponseEntity<List<CartGiftcard>> addGiftCardToCart(@RequestBody CartGiftCardDTO giftcard, Principal principal) {
        String userEmail = principal.getName();
        return ResponseEntity.ok(cartGiftcardService.addGiftCardToCart(giftcard, userEmail));
    }

    @GetMapping
    public ResponseEntity<List<CartGiftcard>> getAllGiftCardsIncartByUser(Principal principal) {
        CustomUser user = userService.validateUser(principal);
        return ResponseEntity.ok(cartGiftcardService.getAllGiftCardsIncartByUser(user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<CartGiftcard>> removeGiftCardFromCart(@PathVariable long id, Principal principal) {
        CustomUser user = userService.validateUser(principal);
        return ResponseEntity.ok(cartGiftcardService.removeGiftCardFromCart(id,user.getId()));
    }

    @DeleteMapping
    public ResponseEntity<String> removeAllGiftCardsFromCart( Principal principal) {
        CustomUser user = userService.validateUser(principal);
        cartGiftcardService.removeAllGiftCardsFromCart(user.getId());
        return ResponseEntity.ok("deleted all cartGiftCards");
    }


}
