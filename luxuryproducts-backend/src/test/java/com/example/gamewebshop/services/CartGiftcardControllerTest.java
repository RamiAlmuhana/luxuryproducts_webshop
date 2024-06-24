
package com.example.gamewebshop.services;

import com.example.gamewebshop.controller.CartGiftcardController;
import com.example.gamewebshop.dto.CartGiftCardDTO;
import com.example.gamewebshop.models.CartGiftcard;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.services.CartGiftcardService;
import com.example.gamewebshop.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class CartGiftcardControllerTest {

    @Mock
    private CartGiftcardService cartGiftcardService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartGiftcardController cartGiftcardController;

    @Mock
    private Principal principal;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_successfully_add_Giftcard_to_cart() {
        CartGiftCardDTO giftcardDTO = new CartGiftCardDTO();
        String userEmail = "example@example.com";
        List<CartGiftcard> cartGiftcards = new ArrayList<>();

        when(principal.getName()).thenReturn(userEmail);
        when(cartGiftcardService.addGiftCardToCart(giftcardDTO, userEmail)).thenReturn(cartGiftcards);

        ResponseEntity<List<CartGiftcard>> response = cartGiftcardController.addGiftCardToCart(giftcardDTO, principal);

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), hasSize(0));

        verify(cartGiftcardService, times(1)).addGiftCardToCart(giftcardDTO, userEmail);
        verifyNoMoreInteractions(cartGiftcardService);
    }

    @Test
    public void should_successfully_remove_a_giftcard_from_cart() {
        CustomUser user = new CustomUser();
        user.setId(1L);
        long giftcardId = 1L;
        List<CartGiftcard> cartGiftcards = new ArrayList<>();

        when(userService.validateUser(principal)).thenReturn(user);
        when(cartGiftcardService.removeGiftCardFromCart(giftcardId, user.getId())).thenReturn(cartGiftcards);

        ResponseEntity<List<CartGiftcard>> response = cartGiftcardController.removeGiftCardFromCart(giftcardId, principal);

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), hasSize(0));

        verify(userService, times(1)).validateUser(principal);
        verify(cartGiftcardService, times(1)).removeGiftCardFromCart(giftcardId, user.getId());
        verifyNoMoreInteractions(userService, cartGiftcardService);
    }

    @Test
    public void should_successfully_remove_all_giftcard_from_the_cart() {
        CustomUser user = new CustomUser();
        user.setId(1L);

        when(userService.validateUser(principal)).thenReturn(user);

        ResponseEntity<String> response = cartGiftcardController.removeAllGiftCardsFromCart(principal);

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is("deleted all cartGiftCards"));

        verify(userService, times(1)).validateUser(principal);
        verify(cartGiftcardService, times(1)).removeAllGiftCardsFromCart(user.getId());
        verifyNoMoreInteractions(userService, cartGiftcardService);
    }


    @Test
    public void should_return_all_giftcard_in_the_cart_that_belong_to_a_user() {
        CustomUser user = new CustomUser();
        user.setId(1L);
        List<CartGiftcard> cartGiftcards = new ArrayList<>();

        when(userService.validateUser(principal)).thenReturn(user);
        when(cartGiftcardService.getAllGiftCardsIncartByUser(user.getId())).thenReturn(cartGiftcards);

        ResponseEntity<List<CartGiftcard>> response = cartGiftcardController.getAllGiftCardsIncartByUser(principal);

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), equalTo(cartGiftcards));

        verify(userService, times(1)).validateUser(principal);
        verify(cartGiftcardService, times(1)).getAllGiftCardsIncartByUser(user.getId());
        verifyNoMoreInteractions(userService, cartGiftcardService);
    }





















}
