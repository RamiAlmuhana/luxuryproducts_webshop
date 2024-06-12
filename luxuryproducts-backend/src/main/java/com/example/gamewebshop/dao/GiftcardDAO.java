package com.example.gamewebshop.dao;

import com.example.gamewebshop.Repositorys.GiftcardRepository;
import com.example.gamewebshop.Repositorys.OrderRepository;
import com.example.gamewebshop.Repositorys.UserRepository;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Giftcard;
import com.example.gamewebshop.models.PlacedOrder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Component
public class GiftcardDAO {

    private final GiftcardRepository giftcardRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Giftcard saveGiftcard(Giftcard giftcard, String userEmail) {
        CustomUser user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        giftcard.setUser(user);
        giftcard = giftcardRepository.save(giftcard);

        // Create a new order for the giftcard
        PlacedOrder order = new PlacedOrder();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(giftcard.getDiscountAmount());
        order.getGiftcards().add(giftcard);
        orderRepository.save(order);

        return giftcard;
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
}
