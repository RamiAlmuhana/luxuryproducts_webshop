package com.example.gamewebshop.services;

import com.example.gamewebshop.Repositorys.CartProductRepository;
import com.example.gamewebshop.Repositorys.ProductRepository;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Product.CartProduct;
import com.example.gamewebshop.models.Product.Enums.CartProductStatus;
import com.example.gamewebshop.models.Product.Product;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CartProductService {

    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;



    public List<CartProduct> addProductToCart(Product product, CustomUser user) {
        Optional<Product> product1 = productRepository.findById(product.getId());
        Optional<List<CartProduct>> cartProduct1 = cartProductRepository.findAllByProductIdAndStatus(product.getId(), CartProductStatus.InCart);
        boolean wentThroughLoop = false;
        if (product1.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Product found with that id"
            );
        }

        for (CartProduct cartProduct : cartProduct1.get()) {
            if (Objects.equals(cartProduct.getImageUrl(), product.getProductVariants().getFirst().getProductImages().getFirst().getImageUrl()) && Objects.equals(product.getProductVariants().getFirst().getProductVariatie().getFirst().getSize().getSize().toString(), cartProduct.getSize())) {
                cartProduct.setQuantity(cartProduct.getQuantity() + product.getQuantity());
                long totalPrice = cartProduct.getProductVariantPrice() * cartProduct.getQuantity();
                cartProduct.setPrice(totalPrice);
                cartProductRepository.save(cartProduct);
                wentThroughLoop = true;
            }

        }

        if (!wentThroughLoop) {
            long totalPrice = product.getProductVariants().getFirst().getPrice() * product.getQuantity();
            CartProduct cartProduct = new CartProduct( product ,product.getQuantity(), totalPrice, product.getProductVariants().getFirst().getProductVariatie().getFirst().getSize().getSize().name(), product.getProductVariants().getFirst().getPrice(), product.getProductVariants().getFirst().getProductImages().getFirst().getImageUrl() ,user, CartProductStatus.InCart);
            cartProductRepository.save(cartProduct);
        }

        Optional<List<CartProduct>> cartProductsByUser = cartProductRepository.findByCustomUserIdAndStatus(user.getId(), CartProductStatus.InCart);
              return cartProductsByUser.get();



    }

    public List<CartProduct> GetProductsInCartByUserId(Long id) {
        Optional<List<CartProduct>> cartProducts = cartProductRepository.findByCustomUserIdAndStatus(id, CartProductStatus.InCart);
        if (cartProducts.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No cart found with that id"
            );
        }
        return cartProducts.get();
    }

    public List<CartProduct> deleteCartProduct(Long id) {

        Optional<CartProduct> cartProduct = cartProductRepository.findById(id);
        if (cartProduct.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No cartProduct found with that id"
            );
        }

        CartProduct cartProduct1 = cartProduct.get();

        if (cartProduct1.getQuantity() == 1) {
            cartProductRepository.deleteById(cartProduct1.getId());
        }else {
            cartProduct1.setQuantity(cartProduct1.getQuantity() - 1);
            cartProduct1.setPrice(cartProduct1.getQuantity() * cartProduct1.getProductVariantPrice());
            cartProductRepository.save(cartProduct1);
        }

        return GetProductsInCartByUserId(cartProduct1.getCustomUser().getId());
    }

    public String deleteAllCartProducts(Long id) {
        Optional<List<CartProduct>> cartProductList = cartProductRepository.findByCustomUserIdAndStatus(id, CartProductStatus.InCart);
        if (cartProductList.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No cart found with user id"
            );
        }
        cartProductRepository.deleteAll(cartProductList.get());

        return "deleted";
    }

    public Boolean checkCartProductStock(Product cartProductnew, Long id) {
        Optional<Product> product1 = productRepository.findById(cartProductnew.getId());
        Optional<List<CartProduct>> cartProduct1 = cartProductRepository.findAllByProductIdAndStatus(cartProductnew.getId(), CartProductStatus.InCart);
        if (product1.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Product found with that id"
            );
        }

        for (CartProduct cartProduct : cartProduct1.get()) {
            if (Objects.equals(cartProduct.getImageUrl(), cartProductnew.getProductVariants().getFirst().getProductImages().getFirst().getImageUrl()) && Objects.equals(cartProductnew.getProductVariants().getFirst().getProductVariatie().getFirst().getSize().getSize().toString(), cartProduct.getSize())) {
                long quantityOfCartProductUserWantsToAdd = cartProduct.getQuantity() + cartProductnew.getQuantity();
                long quantityInStock = cartProductnew.getProductVariants().getFirst().getProductVariatie().getFirst().getQuantity_in_stock();

                return quantityOfCartProductUserWantsToAdd <= quantityInStock;
            }

        }

       return true;

    }
}
