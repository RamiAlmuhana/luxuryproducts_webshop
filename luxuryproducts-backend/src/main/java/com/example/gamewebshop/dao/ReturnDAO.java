package com.example.gamewebshop.dao;

import com.example.gamewebshop.Repositorys.*;
import com.example.gamewebshop.dto.ReturnDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Product.CartProduct;
import com.example.gamewebshop.models.Product.Product;
import com.example.gamewebshop.models.Product.ProductVariatie;
import com.example.gamewebshop.models.ReturnRequest;
import com.example.gamewebshop.services.CartProductService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Component
public class ReturnDAO {
    private final ReturnRepository returnRepository;
    private final UserRepository userRepository;
    private final CartProductRepository cartProductRepository;
    private final CartProductService cartProductService;
    private final OrderDAO orderDAO;






    public List<CustomUser> getAllReturns(){
        return  this.userRepository.findAll();
    }
    @Transactional
    public void saveReturnWithProducts(ReturnDTO return1, String userEmail) {
        CustomUser user = userRepository.findByEmail(userEmail);
        Optional<CartProduct> cartProduct = cartProductRepository.findById(return1.cartProduct.cartproductId);

        if (cartProduct.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No product found with that id"
            );
        }

       CartProduct cartProduct1 = cartProduct.get();

        ReturnRequest returnNew = new ReturnRequest();
        returnNew.setReturnStatus(return1.returnStatus);
        returnNew.setCartProduct(cartProduct1);
        returnNew.setUser(user);
        returnNew.setReturnReason(return1.returnReason);
        cartProduct1.setProductReturned(true);
        cartProductRepository.save(cartProduct1);
        

        returnRepository.save(returnNew);
    }





    public List<ReturnDTO> getReturnsByUserId(){

        List<ReturnDTO> returnDTOS = new ArrayList<>();

        for (ReturnRequest returnRequest : this.returnRepository.findAll()){
            returnDTOS.add(returnRequestConverter(returnRequest));
        }

        return returnDTOS;
    }

    public ReturnDTO returnRequestConverter(ReturnRequest returnRequest){

        ReturnDTO returnDTO = new ReturnDTO();
        returnDTO.returnReason = returnRequest.getReturnReason();
        returnDTO.returnStatus = returnRequest.getReturnStatus();
        returnDTO.cartProduct = orderDAO.orderRetrievalDTOConverter(returnRequest.getCartProduct());
        returnDTO.user = returnRequest.getUser();

        return returnDTO;
    }


    public void updateReturn(String returnStatus, String adminReason, Long id) {
        Optional<ReturnRequest> returnRequest = returnRepository.findById(id);
        if (returnRequest.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No returns found with that id"
            );
        }
        ReturnRequest returnRequest1 = returnRequest.get();
        returnRequest1.setReturnStatus(returnStatus);
        returnRequest1.setAdminReason(adminReason);

        CartProduct cartProduct = cartProductService.getCartProductById(returnRequest1.getCartProduct().getId());

        cartProduct.setReturnStatus(returnStatus);
        cartProductRepository.save(cartProduct);
        returnRepository.save(returnRequest.get());

    }





}
