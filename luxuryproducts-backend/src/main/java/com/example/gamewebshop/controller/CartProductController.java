package com.example.gamewebshop.controller;

import com.example.gamewebshop.Repositorys.UserRepository;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Product.CartProduct;
import com.example.gamewebshop.models.Product.Product;
import com.example.gamewebshop.services.CartProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://s1148232.student.inf-hsleiden.nl:18232"})
@RequestMapping("/cart-product")
public class CartProductController {


    private final CartProductService cartProductService;
    private final UserRepository userRepository;


    @PostMapping
    public ResponseEntity<List<CartProduct>> addProductToCart(@RequestBody Product product, Principal principal){
        if (principal == null) {
            return ResponseEntity.badRequest().build();
        }
        String userEmail = principal.getName();
        CustomUser user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.cartProductService.addProductToCart(product, user));
    }

    @GetMapping
    public ResponseEntity<List<CartProduct>> GetProductsInCartByUserId(Principal principal){
        if (principal == null) {
            return ResponseEntity.badRequest().build();
        }
        String userEmail = principal.getName();
        CustomUser user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

       List<CartProduct> cartProducts =  this.cartProductService.GetProductsInCartByUserId(user.getId());
        return ResponseEntity.ok(cartProducts);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<List<CartProduct>> deleteCartProduct(@PathVariable Long id){


        return ResponseEntity.ok( this.cartProductService.deleteCartProduct(id));
    }
    @DeleteMapping("/deleteall")
    public ResponseEntity<String> deleteCartProduct2(Principal principal){

        if (principal == null) {
            return ResponseEntity.badRequest().build();
        }
        String userEmail = principal.getName();
        CustomUser user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(this.cartProductService.deleteAllCartProducts(user.getId()));
    }

 @PostMapping("/check-cartProduct")
 public ResponseEntity<Boolean> checkCartProductStock(@RequestBody Product product, Principal principal){

     if (principal == null) {
         return ResponseEntity.badRequest().build();
     }
     String userEmail = principal.getName();
     CustomUser user = userRepository.findByEmail(userEmail);
     if (user == null) {
         return ResponseEntity.notFound().build();
     }

     return ResponseEntity.ok(this.cartProductService.checkCartProductStock(product,user.getId()));
 }

    @GetMapping("/totalPrice")
    public ResponseEntity<Long> getTotalPriceOfCartByUser(Principal principal){

        if (principal == null) {
            return ResponseEntity.badRequest().build();
        }
        String userEmail = principal.getName();
        CustomUser user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

       Long totalprice =  this.cartProductService.getTotalPriceOfCartByUser(user.getId());
        return ResponseEntity.ok(totalprice);
    }


}
