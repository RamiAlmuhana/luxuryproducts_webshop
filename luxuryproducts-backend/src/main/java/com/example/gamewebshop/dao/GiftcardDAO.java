package com.example.gamewebshop.dao;

import com.example.gamewebshop.models.Giftcard;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GiftcardDAO {

    private GiftcardRepository giftcardRepository;

    public GiftcardDAO(GiftcardRepository giftcardRepository) {
        this.giftcardRepository = giftcardRepository;
    }

    public Giftcard saveGiftcard(Giftcard giftcard){
        return giftcardRepository.save(giftcard);
    }

    public Optional<Giftcard> validateGiftCard(String code){
        return giftcardRepository.findByCode(code);
    }

    public Optional<Giftcard> useGiftCard(String code){
        Optional<Giftcard> optionalGiftcard = giftcardRepository.findByCode(code);
        if (optionalGiftcard.isPresent()) {
            Giftcard giftcard = optionalGiftcard.get();
            if (!giftcard.isUsed()){
                giftcard.setUsed(true);
                giftcardRepository.save(giftcard);
            }
        }
        return optionalGiftcard;

    }









}
