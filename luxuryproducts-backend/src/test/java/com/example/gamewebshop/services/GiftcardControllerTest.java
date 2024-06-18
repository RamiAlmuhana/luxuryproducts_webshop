

package com.example.gamewebshop.services;

import com.example.gamewebshop.controller.GiftcardController;
import com.example.gamewebshop.dao.GiftcardDAO;
import com.example.gamewebshop.models.Giftcard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class GiftcardControllerTest {


    @Mock
    private GiftcardDAO giftcardDAO;

    @InjectMocks
    private GiftcardController giftcardController;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_return_giftcard_when_giftcard_code_has_been_validated() {
        String code = "SampleCode";
        Giftcard giftcard = new Giftcard();
        giftcard.setCode(code);
        giftcard.setBalance(50);

        when(giftcardDAO.validateGiftCard(code)).thenReturn(Optional.of(giftcard));

        ResponseEntity<Giftcard> response = giftcardController.validateGiftCard(code);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getCode(), is(code));
        assertThat(response.getBody().getBalance(), is(50));

        verify(giftcardDAO, times(1)).validateGiftCard(code);
        verifyNoMoreInteractions(giftcardDAO);
    }

    @Test
    public void should_notifiy_when_giftcard_code_is_invalid(){
        String code = "SampleInvalidCode";

        when(giftcardDAO.validateGiftCard(code)).thenReturn(Optional.empty());

        ResponseEntity<Giftcard> responseEntity = giftcardController.validateGiftCard(code);

        verify(giftcardDAO).validateGiftCard(code);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void should_return_conflict_when_a_giftcard_that_has_been_used_is_being_used_again(){
        String code = "SampleCode";
        Giftcard giftcard = new Giftcard();
        giftcard.setCode(code);
        giftcard.setBalance(0);
        giftcard.setUsed(true);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("code", code);

        when(giftcardDAO.findByCode(code)).thenReturn(Optional.of(giftcard));

        ResponseEntity<Giftcard> response = giftcardController.useGiftCard(requestBody);

        assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));

        verify(giftcardDAO, times(1)).findByCode(code);
        verifyNoMoreInteractions(giftcardDAO);
    }

    @Test
    public void should_return_not_found_when_a_giftcard_has_been_used_with_a_nonexistents_code() {
        String code = "SampleNonExistentCode";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("code", code);

        when(giftcardDAO.findByCode(code)).thenReturn(Optional.empty());

        ResponseEntity<Giftcard> response = giftcardController.useGiftCard(requestBody);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));

        verify(giftcardDAO, times(1)).findByCode(code);
        verifyNoMoreInteractions(giftcardDAO);
    }

    @Test
    public void should_return_okstatus_when_a_giftcard_that_never_has_been_used_before_is_being_used(){
        String code = "SampleCode";
        Giftcard giftcard = new Giftcard();
        giftcard.setCode(code);
        giftcard.setBalance(50);
        giftcard.setUsed(false);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("code", code);

        when(giftcardDAO.findByCode(code)).thenReturn(Optional.of(giftcard));

        ResponseEntity<Giftcard> response = giftcardController.useGiftCard(requestBody);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getCode(), is(code));
        assertThat(response.getBody().getBalance(), is(50));
        assertThat(response.getBody().isUsed(), is(false)); // Confirm the giftcard is still marked as unused

        verify(giftcardDAO, times(1)).findByCode(code);
        verifyNoMoreInteractions(giftcardDAO);
    }












}
