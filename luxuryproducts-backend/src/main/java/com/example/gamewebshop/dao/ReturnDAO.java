package com.example.gamewebshop.dao;

import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Product;
import com.example.gamewebshop.models.ReturnRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class ReturnDAO {
    private final ReturnRepository returnRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReturnDAO(ReturnRepository returnRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.returnRepository = returnRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<CustomUser> getAllReturns(){
        return  this.userRepository.findAll();
    }


    @Transactional
    public void createReturn(ReturnRequest returnRequest, ProductRepository productRepository){
        this.userRepository.save(returnRequest.getUser() );
        this.productRepository.save(returnRequest.getProduct());
        this.returnRepository.save(returnRequest);

    }


    @Transactional
    public void saveReturnWithProducts(ReturnRequest return1, String userEmail) {
        CustomUser user = userRepository.findByEmail(userEmail);
        ReturnRequest returnNew = new ReturnRequest();
        returnNew.setReturnStatus(return1.getReturnStatus());
        returnNew.setProduct(return1.getProduct());
        returnNew.setUser(user);
        Optional<Product> product = productRepository.findById(return1.getProduct().getId());

        if (product.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No product found with that id"
            );
        }

       Product product1 = product.get();
        product1.setProductReturned(true);
        productRepository.save(product1);
        

        returnRepository.save(returnNew);
    }





    public List<ReturnRequest> getReturnsByUserId(){

        return this.returnRepository.findAll();
    }


    public void updateReturn(String returnStatus, Long id) {
        Optional<ReturnRequest> returnRequest = returnRepository.findById(id);
        if (returnRequest.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No returns found with that id"
            );
        }

        returnRequest.get().setReturnStatus(returnStatus);
        returnRepository.save(returnRequest.get());

    }



    public void putProductReturnstatus(long returnId, long orderId) {
        Optional<ReturnRequest> returnRequest = returnRepository.findById(returnId);
        if (returnRequest.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No returns found with that id"
            );
        }

        ReturnRequest returnRequest1 = returnRequest.get();

        Optional<Product> product = productRepository.findById(returnRequest1.getProduct().getId());
        if (product.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No product found with that id"
            );
        }
        Product product1 = product.get();
        product1.setReturnStatus(returnRequest1.getReturnStatus());

       productRepository.save(product1);
    }
}
