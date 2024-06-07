package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.GiftcardDAO;
import com.example.gamewebshop.models.Giftcard;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://s1148232.student.inf-hsleiden.nl:18232"})
@RequestMapping("/giftcards")
public class GiftcardController {
    private GiftcardDAO giftcardDAO;

    public GiftcardController(GiftcardDAO giftcardDAO) {
        this.giftcardDAO = giftcardDAO;
    }

    @PostMapping
    public ResponseEntity<Giftcard> saveGiftcard(@RequestBody Giftcard giftcard){
        return ResponseEntity.ok(giftcardDAO.saveGiftcard(giftcard));
    }

    @PostMapping("/validate")
    public ResponseEntity<Giftcard> validateGiftCard(@RequestBody String code){
        Optional<Giftcard> giftcardOptional = giftcardDAO.validateGiftCard(code);
        if (giftcardOptional.isPresent()){
            return ResponseEntity.ok(giftcardOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/use")
    public ResponseEntity<Giftcard> useGiftCard(@RequestBody String code){
        Optional<Giftcard> giftcardOptional = giftcardDAO.useGiftCard(code);
        if (giftcardOptional.isPresent()){
            Giftcard giftcard = giftcardOptional.get();
            if (giftcard.isUsed()){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else {
                giftcard.setUsed(true);
                giftcardDAO.saveGiftcard(giftcard);
                return ResponseEntity.ok(giftcard);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }








}
