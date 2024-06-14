package com.example.gamewebshop.dao;

import com.example.gamewebshop.Repositorys.CartGiftcardRepositoty;
import com.example.gamewebshop.Repositorys.GiftcardRepository;
import com.example.gamewebshop.Repositorys.OrderRepository;
import com.example.gamewebshop.Repositorys.UserRepository;
import com.example.gamewebshop.dto.CartGiftCardDTO;
import com.example.gamewebshop.models.CartGiftcard;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Giftcard;
import com.example.gamewebshop.models.PlacedOrder;
import com.example.gamewebshop.models.Product.Enums.CartProductStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class GiftcardDAO {

    private final GiftcardRepository giftcardRepository;
    private final UserRepository userRepository;
    private final CartGiftcardRepositoty cartGiftcardRepositoty;


    public Giftcard saveGiftcard(CartGiftCardDTO cartGiftCardDTO, String userEmail) {
        CustomUser user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
        Giftcard giftcard = new Giftcard();
        giftcard.setUser(user);
        giftcard.setBalance(cartGiftCardDTO.discountAmount);
        giftcard.setImageUrl(cartGiftCardDTO.imageUrl);
        giftcard.setDiscountAmount(cartGiftCardDTO.discountAmount);
        giftcard.setUsed(false);
        giftcard.setCode(cartGiftCardDTO.code);


        return giftcardRepository.save(giftcard);
    }

    public Optional<Giftcard> findByCode(String code) {
        return giftcardRepository.findByCode(code);
    }


    public Optional<Giftcard> validateGiftCard(String code) {
        return giftcardRepository.findByCode(code);
    }

    public Optional<Giftcard> useGiftCard(String code) {
        Optional<Giftcard> optionalGiftcard = giftcardRepository.findByCode(code);
        if (optionalGiftcard.isPresent()) {
            Giftcard giftcard = optionalGiftcard.get();
            if (!giftcard.isUsed()) {
                giftcard.setUsed(true);
                giftcardRepository.save(giftcard);
            }
        }
        return optionalGiftcard;
    }


    public List<Giftcard> getGiftcardsFromCartGiftCards(List<CartGiftcard> cartGiftcards) {

        List<Giftcard> giftcards = new ArrayList<>();

        for (CartGiftcard cartGiftcard: cartGiftcards){
            Optional<CartGiftcard> cartGiftcard1 = cartGiftcardRepositoty.findById(cartGiftcard.getId());
            if (cartGiftcard1.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no cartGiftCard found with that id");
            }

            giftcards.add(cartGiftcard1.get().getGiftcard());

        }

        return giftcards;
    }
}
