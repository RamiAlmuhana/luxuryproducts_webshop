package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.GiftcardDAO;
import com.example.gamewebshop.models.Giftcard;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/giftcards")
public class GiftcardController {
    private final GiftcardDAO giftcardDAO;

    public GiftcardController(GiftcardDAO giftcardDAO) {
        this.giftcardDAO = giftcardDAO;
    }


    @PostMapping("/validate")
    public ResponseEntity<Giftcard> validateGiftCard(@RequestBody String code) {
        Optional<Giftcard> giftcardOptional = giftcardDAO.validateGiftCard(code);
        if (giftcardOptional.isPresent()) {
            return ResponseEntity.ok(giftcardOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/use")
    public ResponseEntity<Giftcard> useGiftCard(@RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");
        Optional<Giftcard> giftcardOptional = giftcardDAO.findByCode(code);
        if (giftcardOptional.isPresent()) {
            Giftcard giftcard = giftcardOptional.get();
            if (giftcard.getBalance() > 0 && !giftcard.isUsed()) {
                return ResponseEntity.ok(giftcard);
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
