package com.example.gamewebshop.services;

import com.example.gamewebshop.Repositorys.CartProductRepository;
import com.example.gamewebshop.Repositorys.ProductRepository;
import com.example.gamewebshop.dao.ProductDAO;
import com.example.gamewebshop.models.Product.*;

import com.example.gamewebshop.models.Product.Enums.CartProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartProductServiceTest {

    private Product product;
    private CartProduct cartProduct;

    private List<CartProduct> cartProducts;

   private  CartProductService cartProductService;
   @Mock
    private  CartProductRepository cartProductRepository;
   @Mock
    private  ProductRepository productRepository;
   @Mock
    private  ProductDAO productDAO;
   @Mock
    private  CartGiftcardService cartGiftcardService;

    @BeforeEach
    void setUp() {
        cartProductService = new CartProductService(cartProductRepository, productRepository, productDAO, cartGiftcardService);
        Product product = new Product();

        CartProduct cartProduct = new CartProduct();
        cartProduct.setSize("M");
        cartProduct.setQuantity(30L);

        cartProduct.setImageUrl("test_imageurl_oenkab_oenka");

        ProductVariant productVariant = new ProductVariant();

        ProductVariatie productVariatie = new ProductVariatie();
        productVariatie.setQuantity_in_stock(35L);

        Size size = new Size();
        size.setSize("M");

        ProductImages productImages = new ProductImages();
        productImages.setImageUrl("test_imageurl_oenkab_oenka");

        product.setQuantity(10L);
        productVariant.setProductImages(List.of());
        productVariatie.setSize(size);
        productVariant.setProductImages(List.of(productImages));
        productVariant.setProductVariatie(List.of(productVariatie));
        product.setProductVariants(List.of(productVariant));
         this.cartProducts = List.of(cartProduct);
         this.product = product;
         this.cartProduct = cartProduct;

    }

    @Test
    void should_return_false_when_product_stock_is_less_than_quantity_in_cart_plus_quantity_user_wants_to_add() {

        // arrange
        // in the setup

        // stubs
        when(this.productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        when(this.cartProductRepository.findAllByProductIdAndStatus(anyLong(), any())).thenReturn(Optional.of(this.cartProducts));

        // act
        Boolean result = cartProductService.checkCartProductStock(product, 1L);
        // assert
        assertThat(result).isFalse();

    }


    @Test
    void should_return_true_when_product_stock_is_higher_than_quantity_in_cart_plus_quantity_user_wants_to_add() {

        // arrange
        this.cartProduct.setQuantity(20L);
        this.product.getProductVariants().getFirst().getProductVariatie().getFirst().setQuantity_in_stock(100L);
        this.product.setQuantity(10L);

        // stubs
        when(this.productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        when(this.cartProductRepository.findAllByProductIdAndStatus(anyLong(), any())).thenReturn(Optional.of(this.cartProducts));

        // act
        Boolean result = cartProductService.checkCartProductStock(product, 1L);
        // assert
        assertThat(result).isTrue();

    }

    @Test
    void should_return_expected_value_when_calculating_total_price_of_cart_by_user(){


        // arrange
        this.cartProduct.setPrice(100L);
        Long testId = 1L;
        Long expectedResult = 200L;

        // stubs
        when(cartProductRepository.findByCustomUserIdAndStatus(anyLong(), any())).thenReturn(Optional.of(this.cartProducts));
        when(cartGiftcardService.getTotalPrice(anyLong())).thenReturn(100L);
        // act
        Long result = cartProductService.getTotalPriceOfCartByUser(testId);

        // assert
        assertThat(result).isEqualTo(expectedResult);

        }

    }
