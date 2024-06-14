package com.example.gamewebshop.services;

import com.example.gamewebshop.Repositorys.CartGiftcardRepositoty;
import com.example.gamewebshop.Repositorys.GiftcardRepository;
import com.example.gamewebshop.Repositorys.ProductRepository;
import com.example.gamewebshop.Repositorys.UserRepository;
import com.example.gamewebshop.dao.GiftcardDAO;
import com.example.gamewebshop.dto.CartGiftCardDTO;
import com.example.gamewebshop.models.CartGiftcard;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Giftcard;
import com.example.gamewebshop.models.Product.Enums.CartProductStatus;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CartGiftcardService {
    private CartGiftcardRepositoty cartGiftcardRepositoty;
    private UserRepository userRepository;
    private GiftcardDAO giftcardDAO;
    private GiftcardRepository giftcardRepository;



    public List<CartGiftcard> addGiftCardToCart(CartGiftCardDTO giftcard, String userEmail) {

        CustomUser user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
        Giftcard newGiftCard = giftcardDAO.saveGiftcard(giftcard, userEmail);

        CartGiftcard cartGiftcard = new CartGiftcard();
        cartGiftcard.setGiftcard(newGiftCard);
        cartGiftcard.setUser(user);
        cartGiftcard.setStatus(CartProductStatus.InCart);
        cartGiftcard.setImageUrl(giftcard.imageUrl);

        cartGiftcardRepositoty.save(cartGiftcard);

        return getAllGiftCardsIncartByUser(user.getId());

    }

    public List<CartGiftcard> getAllGiftCardsIncartByUser(long id){
        Optional<CustomUser> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found with that id");
        }
        Optional<List<CartGiftcard>> cartGiftcards = cartGiftcardRepositoty.getCartGiftcardsByUserIdAndStatus(id, CartProductStatus.InCart);

        if (cartGiftcards.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no cart Giftcards found with that userid");
        }

        return cartGiftcards.get();
    }

    public List<CartGiftcard> removeGiftCardFromCart(long cartGiftcardId , Long userId) {
        Optional<CartGiftcard> cartGiftcard = cartGiftcardRepositoty.findById(cartGiftcardId);
        if (cartGiftcard.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no cartGiftCard found with that id");
        }

        Optional<Giftcard> giftcard = giftcardDAO.findByCode(cartGiftcard.get().getGiftcard().getCode());

        if (giftcard.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no giftCard found with that code");
        }

        cartGiftcardRepositoty.delete(cartGiftcard.get());
        giftcardRepository.delete(giftcard.get());

        return getAllGiftCardsIncartByUser(userId);

    }
    public void removeAllGiftCardsFromCart(Long userId) {
        List<CartGiftcard> cartGiftcards = getAllGiftCardsIncartByUser(userId);

        for (CartGiftcard cartGiftcard: cartGiftcards){
            removeGiftCardFromCart(cartGiftcard.getId(), userId);
        }


    }

    public long getTotalPrice(Long userId) {
        List<CartGiftcard> cartGiftcards = getAllGiftCardsIncartByUser(userId);

        if (cartGiftcards.isEmpty()){
            return 0;
        }

        long totalprice = 0;

        for (CartGiftcard cartGiftcard: cartGiftcards){
            totalprice += cartGiftcard.getGiftcard().getDiscountAmount();
        }

        return totalprice;
    }

    public void changeCartGiftCardStatus(List<CartGiftcard> cartGiftcards) {

        for (CartGiftcard cartGiftcard: cartGiftcards){
            Optional<CartGiftcard> cartGiftcard1 = cartGiftcardRepositoty.findById(cartGiftcard.getId());
            if (cartGiftcard1.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no cartGiftCard found with that id");
            }

            cartGiftcard1.get().setStatus(CartProductStatus.Ordered);
            cartGiftcardRepositoty.save(cartGiftcard1.get());

        }

    }
}
