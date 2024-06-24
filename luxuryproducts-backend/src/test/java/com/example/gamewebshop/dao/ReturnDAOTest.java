package com.example.gamewebshop.dao;

import com.example.gamewebshop.Repositorys.CartProductRepository;
import com.example.gamewebshop.Repositorys.ReturnRepository;
import com.example.gamewebshop.Repositorys.UserRepository;
import com.example.gamewebshop.dto.ProductVariantDTOS.OrderRetrievalDTO;
import com.example.gamewebshop.dto.ReturnDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Giftcard;
import com.example.gamewebshop.models.Product.CartProduct;
import com.example.gamewebshop.models.Product.ProductVariant;
import com.example.gamewebshop.models.ReturnRequest;
import com.example.gamewebshop.services.CartProductService;
import jakarta.persistence.Entity;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ReturnDAOTest {

    private String userEmail;
    private ReturnDTO returnDTO;
    private  ReturnDAO returnDAO;
    @Mock
    private OrderDAO orderDAO;
    @Mock
    private  ReturnRepository returnRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  CartProductRepository cartProductRepository;
    @Mock
    private  CartProductService cartProductService;


    @BeforeEach
    void setUp() {
        returnDAO = new ReturnDAO(returnRepository, userRepository, cartProductRepository, cartProductService, orderDAO);
        ReturnDTO returnDTO =  new ReturnDTO();
        OrderRetrievalDTO orderRetrievalDTO = new OrderRetrievalDTO();
        orderRetrievalDTO.cartproductId = 1L;
        returnDTO.cartProduct = orderRetrievalDTO;

        returnDTO.returnStatus = "pending";
        returnDTO.returnReason = "lars heeft het product kapot gemaakt";
        this.returnDTO = returnDTO;
        this.userEmail = "testmail@test.com";

    }

    @Test
    void should_save_returnStatus_with_the_same_value_as_inputed_returnDTO() {
        // arrange (inside setup)

        // stubs
        when(this.userRepository.findByEmail(this.userEmail)).thenReturn(new CustomUser());
        when(this.cartProductRepository.findById(anyLong())).thenReturn(Optional.of(new CartProduct()));

        // act
        returnDAO.saveReturnWithProducts(this.returnDTO, this.userEmail);
        // assert
        ArgumentCaptor<ReturnRequest> returnRequestArgumentCaptor = ArgumentCaptor.forClass(ReturnRequest.class);
        verify(this.returnRepository).save(returnRequestArgumentCaptor.capture());
        ReturnRequest capturedReturn = returnRequestArgumentCaptor.getValue();
        assertThat(capturedReturn.getReturnReason()).isEqualTo(this.returnDTO.returnReason);
    }


    @Test
    void should_throw_HTTP_Not_Found_Exception_When_orderRetrievalDTO_cartproductId_is_invalid() {
        // arrange
        String expectedErrorMessage = "404 NOT_FOUND \"No product found with that id\"";
        // stubs
        when(this.userRepository.findByEmail(this.userEmail)).thenReturn(new CustomUser());
        when(this.cartProductRepository.findById(anyLong())).thenReturn(Optional.empty());

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> returnDAO.saveReturnWithProducts(this.returnDTO, this.userEmail));

        // assert
        MatcherAssert.assertThat(exception.getMessage(), is(expectedErrorMessage));

    }


    @Test
    void should_call_returnRepository_findAll_once_when_calling_function() {

        // arrange
        List<ReturnRequest> returnRequests = new ArrayList<>();

        // stubs
        when(this.returnRepository.findAll()).thenReturn(returnRequests);

        // act
        returnDAO.getAllReturns();

        // assert
        verify(returnRepository, times(1)).findAll();
        verifyNoMoreInteractions(returnRepository);
    }

    @Test
    void should_update_returnStatus_when_updateReturn_is_called() {

        // arrange
        ReturnRequest returnRequest = new ReturnRequest();
        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(21L);
        returnRequest.setCartProduct(cartProduct);
        String returnStatus = "Accepted";
        String adminReason = "product returned in healthy state";
        Long id = 1L;

        // stubs
        when(returnRepository.findById(id)).thenReturn(Optional.of(returnRequest));
        when(cartProductService.getCartProductById(anyLong())).thenReturn(new CartProduct());

        // act
        returnDAO.updateReturn(returnStatus, adminReason, id);

        // assert
        ArgumentCaptor<ReturnRequest> returnRequestArgumentCaptor = ArgumentCaptor.forClass(ReturnRequest.class);
        verify(this.returnRepository).save(returnRequestArgumentCaptor.capture());
        ReturnRequest capturedReturn = returnRequestArgumentCaptor.getValue();
        assertThat(capturedReturn.getReturnStatus()).isEqualTo(returnStatus);

    }

    @Test
    void should_Throw_HTTP_Not_Found_Exception_When_ReturnRequestId_Is_Invalid() {

        // arrange
        String returnStatus = "Accepted";
        String adminReason = "product returned in healthy state";
        Long id = 1L;
        String expectedErrorMessage = "404 NOT_FOUND \"No returns found with that id\"";


        // stubs
        when(returnRepository.findById(id)).thenReturn(Optional.empty());

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> returnDAO.updateReturn(returnStatus, adminReason, id));


        // assert
        MatcherAssert.assertThat(exception.getMessage(), is(expectedErrorMessage));

    }
}